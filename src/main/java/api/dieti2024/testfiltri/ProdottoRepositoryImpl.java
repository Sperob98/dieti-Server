package api.dieti2024.testfiltri;

import api.dieti2024.dto.asta.ricerca.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
@Repository
public class ProdottoRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;

    public ProdottoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List< InfoDatiAstaDTO> getProdottiAstaConFiltroCompleto(FiltroDto filtroDto) {
        //init array without element to avoid null pointer exception
        List<Object> params = new ArrayList<>();
        String query = getQueryPerRicercaConFiltri(filtroDto, params);
        // Creazione array di oggetti contenenti ID e valori
           Object[] paramsArray =params.toArray();
        return jdbcTemplate.query(query, paramsArray,(rs, rowNum) -> {
            InfoDatiAstaDTO infoDatiAstaDTO = new InfoDatiAstaDTO();
            infoDatiAstaDTO.setIdAsta(rs.getInt("id"));
            infoDatiAstaDTO.setIdAsta(rs.getInt("id_asta"));
            infoDatiAstaDTO.setBaseAsta(rs.getDouble("base_asta"));
            infoDatiAstaDTO.setPrezzoAttuale(rs.getDouble("prezzo_attuale"));
            infoDatiAstaDTO.setDataScadenza(rs.getLong("data_scadenza"));
            infoDatiAstaDTO.setDataInizio(rs.getLong("data_inizio"));
            infoDatiAstaDTO.setTipoAsta(rs.getString("tipo"));
            infoDatiAstaDTO.setEmailUtenteCreatore(rs.getString("utente_creatore"));
            infoDatiAstaDTO.setNome(rs.getString("nome_prodotto"));
            String listaImgString = rs.getString("lista_img");
            if (listaImgString != null && !listaImgString.isEmpty()) {
                List<String> listaDiImmagini = Arrays.asList(listaImgString.split(","));
                infoDatiAstaDTO.setImmagini(listaDiImmagini);
            } else {
                infoDatiAstaDTO.setImmagini(List.of("NULL","NULL") ) ;
            }
            infoDatiAstaDTO.setDescrizione(rs.getString("descrizione"));
            infoDatiAstaDTO.setCategoria(rs.getString("categoria"));
            return infoDatiAstaDTO;
        });
        }

    private String getQueryPerRicercaConFiltri(FiltroDto filtroDto, List<Object> params) {
        return getQueryBasePerRicercaConFiltri(filtroDto, params,false);
    }

    private String getQueryBasePerRicercaConFiltri(FiltroDto filtroDto, List<Object> params, Boolean isCount) {
        String where = makeWhere(filtroDto, params);

        if (Boolean.TRUE.equals(isCount)) { // Controllo esplicito per TRUE
            return "SELECT COUNT(*) FROM asta_join_prodotto p " + where;
        }

        String orderBy = makeOrderBy(filtroDto.campoOrdinamento(), filtroDto.direzioneOrdinamento());
        String query = "SELECT p.*, string_agg(i.immagini,', ') as lista_img   FROM asta_join_prodotto p " +
                " LEFT JOIN valore_specifico_di_un_prodotto v ON p.id = v.id_prodotto  LEFT JOIN prodotto_immagini i ON i.prodotto_id=p.id" +
                where +
                " group by p.id, id_asta, base_asta, prezzo_attuale, data_scadenza, data_inizio, tipo, utente_creatore, nome_prodotto, p.immagini, descrizione, categoria " +
                orderBy;

        query = addQueryLimiteAndOffset(filtroDto, params, query);
        return query;
    }


    private String makeOrderBy(CampoOrdinamento campoOrdinamento, DirezioneOrdinamento direzioneOrdinamento) {
        if (campoOrdinamento == null || direzioneOrdinamento == null) {
            return " ";
        }
        return " ORDER BY " + campoOrdinamento.getCampo() + " " + direzioneOrdinamento.getDirezione();
    }

    private static String addQueryLimiteAndOffset(FiltroDto filtroDto, List<Object> params, String query) {
        query +=        " LIMIT ? OFFSET ?" ;
        params.add(filtroDto.elementiPerPagina());
        int offset = (filtroDto.pagina() - 1) * filtroDto.elementiPerPagina();
        params.add(offset);
        return query;
    }




    private String makeWhere(FiltroDto filtroDto, List<Object> params) {
        StringBuilder stringBuilder = new StringBuilder();

        addPriceFilters(filtroDto, params, stringBuilder);
        addCaratteristicheFilter(filtroDto, params, stringBuilder);

        if (isNotEmpty(filtroDto.categoria()) && !filtroDto.categoria().equals("tutte")) {
            addCategoryFilter(filtroDto, params, stringBuilder);
        }

        if (filtroDto.tipoAsta()!=null && !filtroDto.tipoAsta().isEmpty()) {
            addTipoAstaFilter(filtroDto, params, stringBuilder);
        }

        if (isNotEmpty(filtroDto.nomeProdotto())) {
            addNomeProdottoFilter(filtroDto, params, stringBuilder);
        }

        return finalizeWhereClause(stringBuilder);
    }

    private void addPriceFilters(FiltroDto filtroDto, List<Object> params, StringBuilder sb) {
        if (filtroDto.prezzoMin() != null && !filtroDto.prezzoMin().isNaN()) {
            sb.append("prezzo_attuale >= ? AND ");
            params.add(filtroDto.prezzoMin());
        }
        if (filtroDto.prezzoMax() != null && !filtroDto.prezzoMax().isNaN()) {
            sb.append("prezzo_attuale <= ? AND ");
            params.add(filtroDto.prezzoMax());
        }
    }

    private void addCaratteristicheFilter(FiltroDto filtroDto, List<Object> params, StringBuilder sb) {
        if (filtroDto.caratteristicheSelezionate() != null && !filtroDto.caratteristicheSelezionate().isEmpty()) {
            sb.append(getClausolaWherePerValoriSpecifici(filtroDto.caratteristicheSelezionate(), params));
        }
    }

    private void addCategoryFilter(FiltroDto filtroDto, List<Object> params, StringBuilder sb) {
        sb.append(" categoria IN (SELECT * FROM TrovaFigliCategoria(?)) AND ");
        params.add(filtroDto.categoria());
    }

    private void addTipoAstaFilter(FiltroDto filtroDto, List<Object> params, StringBuilder sb) {
        String puntiIterogativi = createQuestionMarks(filtroDto.tipoAsta());
        sb.append("tipo IN (").append(puntiIterogativi).append(") AND ");
        params.addAll(filtroDto.tipoAsta());
    }

    private void addNomeProdottoFilter(FiltroDto filtroDto, List<Object> params, StringBuilder sb) {
        sb.append("nome_prodotto ILIKE ? AND ");
        params.add("%" + filtroDto.nomeProdotto() + "%");
    }

    private String finalizeWhereClause(StringBuilder sb) {
        int lastIndex = sb.lastIndexOf("AND");
        if (lastIndex != -1) {
            sb.delete(lastIndex, sb.length());
        }
        return sb.isEmpty() ? "" : " WHERE " + sb;
    }

    private boolean isNotEmpty(String s) {
        return s != null && !s.isEmpty();
    }



    private String getClausolaWherePerValoriSpecifici( List<FiltroCaratteristicheDTO> filtroCaratteristicheDTOS, List<Object> params) {
        if (filtroCaratteristicheDTOS==null || filtroCaratteristicheDTOS.isEmpty()) {
            return "";
        }
        StringBuilder where = new StringBuilder(" p.id IN ( ");
        //foreach filtroCaratteristicheDTOS
        int count = 0;
        for(FiltroCaratteristicheDTO caratteristica : filtroCaratteristicheDTOS){
            count++;
            where.append(" select id_prodotto from valore_specifico_di_un_prodotto v2 where  id_caratteristica = ? and valore in ( ");
            // ?, ?, ? ... FOR EACH ELEMENT IN caratteristiche.get(i)
            where.append(createQuestionMarks(caratteristica.valoriSelezionati()));
            where.append(" )     ");
            params.add(caratteristica.idCaratteristica());
            params.addAll(caratteristica.valoriSelezionati());
            if (count < filtroCaratteristicheDTOS.size()) {
                where.append(" INTERSECT  ");
            }

        }
        where.append(") AND  ");
        return where.toString();
    }

    public String createQuestionMarks(List<String> list) {
        List<String> questionMarks = Collections.nCopies(list.size(), "?");
        return String.join(", ", questionMarks);
    }


    public int getNumeroProdottiAstaConFiltroCompleto(FiltroDto filtroDto) {
        //init array without element to avoid null pointer exception
        List<Object> params = new ArrayList<>();
        String query = getQueryBasePerRicercaConFiltri(filtroDto, params,true);
        // Creazione array di oggetti contenenti ID e valori
        Object[] paramsArray =params.toArray();
        Integer result = jdbcTemplate.queryForObject(query, paramsArray, Integer.class);
        return (result != null) ? result : 0;
    }
}