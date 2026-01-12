package it.unical.fleetgo.backend;

import it.unical.fleetgo.backend.Controller.AdminAziendale.*;
import it.unical.fleetgo.backend.Models.DTO.*;
import it.unical.fleetgo.backend.Service.AdminAziendaleService;
import it.unical.fleetgo.backend.Service.AziendaService;
import it.unical.fleetgo.backend.Service.UtenteService;
import it.unical.fleetgo.backend.Service.VeicoloService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = ControllerDashboard.class)
public class AdminAziendaleTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminAziendaleService service;

    private static void setField(Object target, String name, Object value) {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nested
    class ControllerModificaDatiTests {
        private MockMvc mockMvc;
        private AdminAziendaleService adminService;
        private UtenteService utenteService;
        private AziendaService aziendaService;

        @BeforeEach
        void setup() {
            ControllerModificaDati controller = new ControllerModificaDati();
            adminService = Mockito.mock(AdminAziendaleService.class);
            utenteService = Mockito.mock(UtenteService.class);
            aziendaService = Mockito.mock(AziendaService.class);
            setField(controller, "adminAziendaleService", adminService);
            setField(controller, "utenteService", utenteService);
            setField(controller, "aziendaService", aziendaService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("modificaDatiAdmin: 401 without idUtente, 200 with idUtente and delegates")
        void modificaDatiAdmin_authAndDelegates() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/modificaDatiAdmin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(post("/dashboardAdminAziendale/modificaDatiAdmin")
                            .sessionAttr("idUtente", 10)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nome\":\"N\",\"cognome\":\"C\",\"data\":\"2000-01-01\",\"email\":\"e@x\",\"nomeAzienda\":\"A\",\"sedeAzienda\":\"S\",\"pIva\":\"IT123\",\"idUtente\":0}"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Dati modificati")));

            ArgumentCaptor<ModificaDatiUtenteDTO> captor = ArgumentCaptor.forClass(ModificaDatiUtenteDTO.class);
            Mockito.verify(adminService).modificaDati(captor.capture());
            Assertions.assertEquals(10, captor.getValue().getIdUtente());
        }

        @Test
        @DisplayName("datiUtente: 401 without idUtente, 200 with body when present")
        void datiUtente_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/datiUtente"))
                    .andExpect(status().isUnauthorized());

            ModificaDatiUtenteDTO dto = new ModificaDatiUtenteDTO("N","C","2000-01-01","e@x","A","S","IT123",3);
            Mockito.when(utenteService.getDatiUtente(3)).thenReturn(dto);

            mockMvc.perform(get("/dashboardAdminAziendale/datiUtente")
                            .sessionAttr("idUtente", 3))
                    .andExpect(status().isOk());

            Mockito.verify(utenteService).getDatiUtente(3);
        }

        @Test
        @DisplayName("luoghiAzienda: 401 without idAzienda, 200 with list")
        void luoghiAzienda_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/luoghiAzienda"))
                    .andExpect(status().isUnauthorized());

            Mockito.when(adminService.getLuoghiCorrenti(4)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/luoghiAzienda").sessionAttr("idAzienda", 4))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getLuoghiCorrenti(4);
        }

        @Test
        @DisplayName("aggiungiLuogo: 401 without idAzienda, 200 sets idAzienda and delegates")
        void aggiungiLuogo_authAndDelegates() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/aggiungiLuogo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(post("/dashboardAdminAziendale/aggiungiLuogo")
                            .sessionAttr("idAzienda", 22)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"nome\":\"Sede\"}"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Luogo aggiunto")));
            ArgumentCaptor<LuogoDTO> captor = ArgumentCaptor.forClass(LuogoDTO.class);
            Mockito.verify(adminService).aggiungiLuogo(captor.capture());
        }

        @Test
        @DisplayName("eliminaLuogo: returns 200 when service true, 500 when false")
        void eliminaLuogo_statusByServiceResult() throws Exception {
            Mockito.when(adminService.eliminaLuogo(5)).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/eliminaLuogo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("5"))
                    .andExpect(status().isOk());

            Mockito.when(adminService.eliminaLuogo(6)).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/eliminaLuogo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("6"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("impostaSede: 401 without idAzienda, 200/500 based on service")
        void impostaSede_authAndResult() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/impostaSede")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("10"))
                    .andExpect(status().isUnauthorized());

            Mockito.when(aziendaService.impostaSede(10, 2)).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/impostaSede")
                            .sessionAttr("idAzienda", 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("10"))
                    .andExpect(status().isOk());

            Mockito.when(aziendaService.impostaSede(11, 2)).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/impostaSede")
                            .sessionAttr("idAzienda", 2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("11"))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class ControllerPrenotazioniTests {
        private MockMvc mockMvc;
        private AdminAziendaleService adminService;

        @BeforeEach
        void setup() {
            ControllerPrenotazioni controller = new ControllerPrenotazioni();
            adminService = Mockito.mock(AdminAziendaleService.class);
            setField(controller, "adminAziendaleService", adminService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("getPrenotazioni: 401 without idAzienda, 200 with list")
        void getPrenotazioni_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getPrenotazioni")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getPrenotazioni(9)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/getPrenotazioni").sessionAttr("idAzienda", 9))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getPrenotazioni(9);
        }

        @Test
        @DisplayName("getPrenotazioni/{id}: 200 and delegates")
        void getPrenotazioneById_ok() throws Exception {
            Mockito.when(adminService.getRichiestaNoleggio(7)).thenReturn(new RichiestaNoleggioDTO());
            mockMvc.perform(get("/dashboardAdminAziendale/getPrenotazioni/7"))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getRichiestaNoleggio(7);
        }

        @Test
        @DisplayName("approva/rifiuta: 200 on true, 500 on false")
        void approveAndReject_statusBasedOnService() throws Exception {
            Mockito.when(adminService.approvaRichiestaNoleggio(3)).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/approvaRichiesta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("3"))
                    .andExpect(status().isOk());

            Mockito.when(adminService.rifiutaRichiesta(4)).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/rifiutaRichiesta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("4"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("accettazioneConRifiuto: 200 and delegates to service")
        void accettazioneConRifiuto_ok() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/accettazioneConRifiuto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"idRichiestaDaApprovare\":5,\"idRichiesteDaRifiutare\":[1,2,3]}"))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).approvazioneConRifiutoAutomatico(Mockito.eq(5), Mockito.eq(Arrays.asList(1,2,3)));
        }
    }

    @Nested
    class ControllerStoricoFattureTests {
        private MockMvc mockMvc;
        private AdminAziendaleService adminService;

        @BeforeEach
        void setup() {
            ControllerStoricoFatture controller = new ControllerStoricoFatture();
            adminService = Mockito.mock(AdminAziendaleService.class);
            setField(controller, "adminAziendaleService", adminService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("getFattureEmesse: 401 without idAzienda, 200 with list")
        void getFattureEmesse_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getFattureEmesse")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getFattureEmesse(3)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/getFattureEmesse").sessionAttr("idAzienda", 3))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getFattureEmesse(3);
        }

        @Test
        @DisplayName("downloadFattura: 401 without idAzienda, 200 with pdf headers")
        void downloadFattura_authAndHeaders() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/downloadFattura/10")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.downloadFattura(10, 1)).thenReturn(new byte[]{1,2,3});
            mockMvc.perform(get("/dashboardAdminAziendale/downloadFattura/10").sessionAttr("idAzienda", 1))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", containsString("application/pdf")))
                    .andExpect(header().string("Content-Disposition", containsString("Fattura_FleetGo_10.pdf")));
        }

        @Test
        @DisplayName("pagaFattura: 401 without idAzienda, 200 with url")
        void pagaFattura_authAndOk() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/pagaFattura/5")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.pagaFattura(5, 2)).thenReturn("https://pay");
            mockMvc.perform(post("/dashboardAdminAziendale/pagaFattura/5").sessionAttr("idAzienda", 2))
                    .andExpect(status().isOk())
                    .andExpect(content().string("https://pay"));
        }

        @Test
        @DisplayName("fatturaPagata: 200 on true, 500 on false")
        void fatturaPagata_resultBasedOnService() throws Exception {
            Mockito.when(adminService.contrassegnaFatturaPagata(7)).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/fatturaPagata/7"))
                    .andExpect(status().isOk());

            Mockito.when(adminService.contrassegnaFatturaPagata(8)).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/fatturaPagata/8"))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("getAnniDisponibili: 401 without idAzienda, 200 with list")
        void getAnniDisponibili_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getAnniDisponibili")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getAnniFatture(6)).thenReturn(Arrays.asList(2023, 2024));
            mockMvc.perform(get("/dashboardAdminAziendale/getAnniDisponibili").sessionAttr("idAzienda", 6))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
        }
    }

    @Nested
    class ControllerFlottaAziendaleTests {
        private MockMvc mockMvc;
        private VeicoloService veicoloService;
        private AdminAziendaleService adminService;

        @BeforeEach
        void setup() {
            ControllerFlottaAziendale controller = new ControllerFlottaAziendale();
            veicoloService = Mockito.mock(VeicoloService.class);
            adminService = Mockito.mock(AdminAziendaleService.class);
            setField(controller, "veicoloService", veicoloService);
            setField(controller, "adminAziendaleService", adminService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("flottaAziendale: 401 without idAzienda, 200 with list")
        void flottaAziendale_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/flottaAziendale")).andExpect(status().isUnauthorized());

            Mockito.when(veicoloService.getListaVeicoliAziendali(9)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/flottaAziendale").sessionAttr("idAzienda", 9))
                    .andExpect(status().isOk());
            Mockito.verify(veicoloService).getListaVeicoliAziendali(9);
        }

        @Test
        @DisplayName("impostaLuogo: 401 without idAzienda, 200 delegates and sets idAziendaAffiliata")
        void impostaLuogo_authAndDelegate() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/impostaLuogo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(post("/dashboardAdminAziendale/impostaLuogo")
                            .sessionAttr("idAzienda", 55)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isOk());
            ArgumentCaptor<VeicoloDTO> captor = ArgumentCaptor.forClass(VeicoloDTO.class);
            Mockito.verify(veicoloService).impostaLuogoVeicolo(captor.capture());
            Assertions.assertEquals(55, captor.getValue().getIdAziendaAffiliata());
        }

        @Test
        @DisplayName("inserisciRichiestaManutenzione: 401 without idUtente, 200 with id set and delegates")
        void inserisciRichiestaManutenzione_authAndOk() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/inserisciRichiestaManutenzione")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(post("/dashboardAdminAziendale/inserisciRichiestaManutenzione")
                            .sessionAttr("idUtente", 7)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isOk());
            ArgumentCaptor<RichiestaManutenzioneDTO> cap = ArgumentCaptor.forClass(RichiestaManutenzioneDTO.class);
            Mockito.verify(adminService).inserisciRichiestaDiManutenzione(cap.capture());
            Assertions.assertEquals(7, cap.getValue().getIdAdminAzienda());
        }

        @Test
        @DisplayName("getRichiestaManutenzione: 401 without idUtente, 200 with body and delegates")
        void getRichiestaManutenzione_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiestaManutenzione/3")).andExpect(status().isUnauthorized());

            Mockito.when(veicoloService.getManutenzioneVeicolo(3, 12)).thenReturn(new RichiestaManutenzioneDTO());
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiestaManutenzione/3").sessionAttr("idUtente", 12))
                    .andExpect(status().isOk());
            Mockito.verify(veicoloService).getManutenzioneVeicolo(3, 12);
        }

        @Test
        @DisplayName("eliminaRichiesta: 200 when service true, 400 when false")
        void eliminaRichiesta_statusByService() throws Exception {
            Mockito.when(veicoloService.eliminaRichiestaManutenzione(Mockito.any())).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/eliminaRichiesta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isOk());

            Mockito.when(veicoloService.eliminaRichiestaManutenzione(Mockito.any())).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/eliminaRichiesta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ControllerGestioneVeicoloTests {
        private MockMvc mockMvc;
        private VeicoloService veicoloService;

        @BeforeEach
        void setup() {
            ControllerGestioneVeicolo controller = new ControllerGestioneVeicolo();
            veicoloService = Mockito.mock(VeicoloService.class);
            setField(controller, "veicoloService", veicoloService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("informazioneVeicolo: 200 and delegates to service")
        void informazioneVeicolo_ok() throws Exception {
            Mockito.when(veicoloService.getInformazioniVeicolo("AB123CD")).thenReturn(new VeicoloDTO());
            mockMvc.perform(get("/dashboardAdminAziendale/informazioneVeicolo/AB123CD"))
                    .andExpect(status().isOk());
            Mockito.verify(veicoloService).getInformazioniVeicolo("AB123CD");
        }
    }

    @Nested
    class ControllerGestioneDipendentiTests {
        private MockMvc mockMvc;
        private AdminAziendaleService adminService;

        @BeforeEach
        void setup() {
            ControllerGestioneDipendenti controller = new ControllerGestioneDipendenti();
            adminService = Mockito.mock(AdminAziendaleService.class);
            setField(controller, "adminAziendaleService", adminService);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }

        @Test
        @DisplayName("getDipendenti: 401 without idAzienda, 200 with list")
        void getDipendenti_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getDipendenti")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getDipendenti(5)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/getDipendenti").sessionAttr("idAzienda", 5))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getDipendenti(5);
        }

        @Test
        @DisplayName("rimuoviDipendente: 401 without idAzienda, 400 when service false, 200 when true")
        void rimuoviDipendente_authAndStatus() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/rimuoviDipendente")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("10"))
                    .andExpect(status().isUnauthorized());

            Mockito.when(adminService.rimuoviDipendente(10, 1)).thenReturn(false);
            mockMvc.perform(post("/dashboardAdminAziendale/rimuoviDipendente")
                            .sessionAttr("idAzienda", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("10"))
                    .andExpect(status().isBadRequest());

            Mockito.when(adminService.rimuoviDipendente(10, 1)).thenReturn(true);
            mockMvc.perform(post("/dashboardAdminAziendale/rimuoviDipendente")
                            .sessionAttr("idAzienda", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("getRichiesteNoleggio: 401 without idAzienda, 200 with list")
        void getRichiesteNoleggio_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiesteNoleggio/2")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getRichiesteNoleggio(2, 3)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiesteNoleggio/2").sessionAttr("idAzienda", 3))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getRichiesteNoleggio(2, 3);
        }

        @Test
        @DisplayName("getRichiesteAffiliazione: 401 without idAzienda, 200 with list")
        void getRichiesteAffiliazione_authAndOk() throws Exception {
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiesteAffiliazione")).andExpect(status().isUnauthorized());

            Mockito.when(adminService.getRichiesteAffiliazioneDaAccettare(9)).thenReturn(Collections.emptyList());
            mockMvc.perform(get("/dashboardAdminAziendale/getRichiesteAffiliazione").sessionAttr("idAzienda", 9))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).getRichiesteAffiliazioneDaAccettare(9);
        }

        @Test
        @DisplayName("rispondiAffiliazione: 401 without idAzienda, 200 delegates with correct args")
        void rispondiAffiliazione_authAndOk() throws Exception {
            mockMvc.perform(post("/dashboardAdminAziendale/rispondiAffiliazione/77")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("true"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(post("/dashboardAdminAziendale/rispondiAffiliazione/77")
                            .sessionAttr("idAzienda", 6)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("false"))
                    .andExpect(status().isOk());
            Mockito.verify(adminService).rispondiRichiestaAffiliazione(77, 6, false);
        }
    }

    @Test
    @DisplayName("getOfferteAttive: returns 200 with offers list")
    void getOfferteAttive_returnsOffers() throws Exception {
        OffertaDTO o1 = new OffertaDTO();
        o1.setIdOfferta(1);
        o1.setNomeOfferta("Promo");
        List<OffertaDTO> offers = Arrays.asList(o1);
        Mockito.when(service.getOfferteAttive()).thenReturn(offers);

        mockMvc.perform(get("/dashboardAdminAziendale/getOfferte").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idOfferta", is(1)))
                .andExpect(jsonPath("$[0].nomeOfferta", is("Promo")));

        Mockito.verify(service).getOfferteAttive();
    }

    @Test
    @DisplayName("getNumeroRichiesteAffiliazione: 401 when idAzienda missing")
    void getNumeroRichiesteAffiliazione_unauthorizedWhenMissingSession() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getContatoreRichiesteAffiliazione").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        Mockito.verify(service, Mockito.never()).getNumRichiesteAffiliazione(Mockito.anyInt());
    }

    @Test
    @DisplayName("getNumeroRichiesteAffiliazione: 200 and delegates to service")
    void getNumeroRichiesteAffiliazione_ok() throws Exception {
        Mockito.when(service.getNumRichiesteAffiliazione(Mockito.eq(7))).thenReturn(3);

        mockMvc.perform(get("/dashboardAdminAziendale/getContatoreRichiesteAffiliazione")
                        .sessionAttr("idAzienda", 7)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        Mockito.verify(service).getNumRichiesteAffiliazione(7);
    }

    @Test
    @DisplayName("getSpesaMensile: 401 when idAzienda missing, 200 otherwise")
    void getSpesaMensile_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getSpesaMensile").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.getSpesaMensile(5)).thenReturn(42.5f);
        mockMvc.perform(get("/dashboardAdminAziendale/getSpesaMensile")
                        .sessionAttr("idAzienda", 5)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("42.5"));
        Mockito.verify(service).getSpesaMensile(5);
    }

    @Test
    @DisplayName("getNumeroAutoSenzaLuogo: 401 when idAzienda missing, 200 and value otherwise")
    void getNumeroAutoSenzaLuogo_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getNumeroAutoSenzaLuogo").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.getNumeroAutoSenzaLuogo(9)).thenReturn(11);
        mockMvc.perform(get("/dashboardAdminAziendale/getNumeroAutoSenzaLuogo")
                        .sessionAttr("idAzienda", 9)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("11"));
        Mockito.verify(service).getNumeroAutoSenzaLuogo(9);
    }

    @Test
    @DisplayName("getDatiGraficoTorta: 401 when idAzienda missing, 200 and body otherwise")
    void getDatiGraficoTorta_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getDatiGraficoTorta").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        ContenitoreStatisticheNumeriche stats = new ContenitoreStatisticheNumeriche(4, 8, 3);
        Mockito.when(service.getDatiGraficoTorta(12)).thenReturn(stats);

        mockMvc.perform(get("/dashboardAdminAziendale/getDatiGraficoTorta")
                        .sessionAttr("idAzienda", 12)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.veicoliNoleggiati", is(4)))
                .andExpect(jsonPath("$.veicoliDisponibili", is(8)))
                .andExpect(jsonPath("$.veicoliManutenzione", is(3)));

        Mockito.verify(service).getDatiGraficoTorta(12);
    }

    @Test
    @DisplayName("getNomeCognomeAdmin: 401 when idUtente missing, 200 with body otherwise")
    void getNomeCognomeAdmin_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getNomeCognomeAdmin").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.getNomeCognomeAdmin(2)).thenReturn("Mario Rossi");
        mockMvc.perform(get("/dashboardAdminAziendale/getNomeCognomeAdmin")
                        .sessionAttr("idUtente", 2))
                .andExpect(status().isOk())
                .andExpect(content().string("Mario Rossi"));
        Mockito.verify(service).getNomeCognomeAdmin(2);
    }

    @Test
    @DisplayName("getNomeAziendaGestita: 401 when idAzienda missing, 200 otherwise")
    void getNomeAziendaGestita_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getNomeAziendaGestita").accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.getNomeAziendaGestita(44)).thenReturn("ACME");
        mockMvc.perform(get("/dashboardAdminAziendale/getNomeAziendaGestita")
                        .sessionAttr("idAzienda", 44))
                .andExpect(status().isOk())
                .andExpect(content().string("ACME"));
        Mockito.verify(service).getNomeAziendaGestita(44);
    }

    @Test
    @DisplayName("richiediAppuntamento: 200 and delegates when idUtente present")
    void richiediAppuntamento_ok() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/dashboardAdminAziendale/richiediAppuntamento")
                        .sessionAttr("idUtente", 77))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Richiesta inviata")));
        Mockito.verify(service).richiediAppuntamento(77);
    }

    @Test
    @DisplayName("getNumeroFattureDaPagare: 401 when idAzienda missing, 200 with int otherwise")
    void getNumeroFattureDaPagare_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/getNumFattureDaPagare").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.getNumeroFattureDaPagare(5)).thenReturn(2);
        mockMvc.perform(get("/dashboardAdminAziendale/getNumFattureDaPagare")
                        .sessionAttr("idAzienda", 5))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
        Mockito.verify(service).getNumeroFattureDaPagare(5);
    }

    @Test
    @DisplayName("isSedeImpostata: 401 when idAzienda missing, 200 with boolean otherwise")
    void isSedeImpostata_authAndOk() throws Exception {
        mockMvc.perform(get("/dashboardAdminAziendale/isSedeImpostata").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        Mockito.when(service.isSedeImpostata(66)).thenReturn(true);
        mockMvc.perform(get("/dashboardAdminAziendale/isSedeImpostata")
                        .sessionAttr("idAzienda", 66))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        Mockito.verify(service).isSedeImpostata(66);
    }
}
