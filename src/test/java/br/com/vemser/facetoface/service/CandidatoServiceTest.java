//package br.com.vemser.facetoface.service;
//
//import br.com.vemser.facetoface.dto.*;
//import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
//import br.com.vemser.facetoface.dto.candidato.CandidatoDTO;
//import br.com.vemser.facetoface.dto.paginacaodto.PageDTO;
//import br.com.vemser.facetoface.entity.CandidatoEntity;
//import br.com.vemser.facetoface.entity.EdicaoEntity;
//import br.com.vemser.facetoface.entity.LinguagemEntity;
//import br.com.vemser.facetoface.entity.TrilhaEntity;
//import br.com.vemser.facetoface.entity.enums.Genero;
//import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
//import br.com.vemser.facetoface.repository.CandidatoRepository;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CandidatoServiceTest {
//    @InjectMocks
//    private CandidatoService candidatoService;
//    @Mock
//    private CandidatoRepository candidatoRepository;
//    @Mock
//    private LinguagemService linguagemService;
//    @Mock
//    private EdicaoService edicaoService;
//    @Mock
//    private TrilhaService trilhaService;
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Before
//    public void init() {
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        ReflectionTestUtils.setField(candidatoService, "objectMapper", objectMapper);
//    }
//
//    @Test
//    public void deveCadastrarUmCandidatoCorretamente() throws RegraDeNegocioException {
//        final String emailEsperado = "heloise.lopes@dbccompany.com.br";
//        final String trilhaEsperado = "BACKEND";
//        final int linguagensTamanho = 1;
//
//        CandidatoCreateDTO candidatoCreateDTO = getCandidatoCreateDTO();
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//        LinguagemEntity linguagemEntity = getLinguagemEntity();
//        TrilhaEntity trilha = getTrilhaEntity();
//        EdicaoEntity edicao = getEdicaoEntity();
//
//        when(candidatoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(linguagemService.findByNome(anyString())).thenReturn(linguagemEntity);
//        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
//        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
//        when(candidatoRepository.save(any())).thenReturn(candidatoEntity);
//
//        CandidatoDTO candidatoDTO = candidatoService.create(candidatoCreateDTO, Genero.FEMININO);
//
//        assertEquals(emailEsperado, candidatoDTO.getEmail());
//        assertEquals(trilhaEsperado, candidatoDTO.getTrilha().getNome());
//        assertEquals(linguagensTamanho, candidatoDTO.getLinguagens().size());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoCadastrarCandidatoEJaExistirCandidadoComMesmoEmail() throws RegraDeNegocioException {
//        CandidatoCreateDTO candidatoCreateDTO = getCandidatoCreateDTO();
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findByEmail(anyString())).thenReturn(Optional.of(candidatoEntity));
//
//        candidatoService.create(candidatoCreateDTO, Genero.FEMININO);
//    }
//
//    @Test
//    public void deveRetornarUmaListaPaginada() {
//        final int pagina = 0;
//        final int tamanho = 5;
//
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//        PageImpl<CandidatoEntity> candidatoEntities =
//                new PageImpl<>(List.of(candidatoEntity), PageRequest.of(pagina, tamanho), 0);
//
//        when(candidatoRepository.findAll(any(Pageable.class))).thenReturn(candidatoEntities);
//
//        PageDTO<CandidatoDTO> candidatoDTOPageDTO = candidatoService.list(pagina, tamanho);
//
//        assertEquals(pagina, candidatoDTOPageDTO.getPagina());
//        assertEquals(1, candidatoDTOPageDTO.getElementos().size());
//        assertEquals(tamanho, candidatoDTOPageDTO.getTamanho());
//    }
//
//    @Test
//    public void deveDeletarUmCandidatoCorretamente() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findById(any())).thenReturn(Optional.of(candidatoEntity));
//        candidatoService.delete(1);
//
//        verify(candidatoRepository).save(any());
//    }
//
//    @Test
//    public void deveAtualizarUmCandidatoCorretamente() throws RegraDeNegocioException {
//        CandidatoCreateDTO candidatoCreateDTO = getCandidatoCreateDTO();
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//        LinguagemEntity linguagemEntity = getLinguagemEntity();
//        TrilhaEntity trilha = getTrilhaEntity();
//        EdicaoEntity edicao = getEdicaoEntity();
//
//        CandidatoEntity candidatoSalvo = getCandidatoEntity();
//
//        when(candidatoRepository.findById(any())).thenReturn(Optional.of(candidatoEntity));
//        when(linguagemService.findByNome(anyString())).thenReturn(linguagemEntity);
//        when(trilhaService.findByNome(anyString())).thenReturn(trilha);
//        when(edicaoService.findByNome(anyString())).thenReturn(edicao);
//        when(candidatoRepository.save(any())).thenReturn(candidatoSalvo);
//
//        CandidatoDTO candidatoDTO = candidatoService.update(1, candidatoCreateDTO, Genero.FEMININO);
//
//        assertEquals(candidatoSalvo.getEmail(), candidatoDTO.getEmail());
//        assertEquals(candidatoSalvo.getLinguagens().size(), candidatoDTO.getLinguagens().size());
//    }
//
//    @Test
//    public void deveBuscarCandidatoDTOPorEmailCorretamente() throws RegraDeNegocioException {
//        final String emailEsperado = "heloise.lopes@dbccompany.com.br";
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findByEmail(anyString())).thenReturn(Optional.of(candidatoEntity));
//        CandidatoDTO candidatoDTO = candidatoService.findByEmail(emailEsperado);
//
//        assertEquals(emailEsperado, candidatoDTO.getEmail());
//    }
//
//    @Test
//    public void deveBuscarCandidatoPeloEmailCorretamente() throws RegraDeNegocioException {
//        final String emailEsperado = "heloise.lopes@dbccompany.com.br";
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findByEmail(anyString())).thenReturn(Optional.of(candidatoEntity));
//        CandidatoEntity candidato = candidatoService.findByEmailEntity(emailEsperado);
//
//        assertEquals(emailEsperado, candidato.getEmail());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoEmailBuscadoNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
//        final String emailEsperado = "heloise.lopes@dbccompany.com.br";
//
//        when(candidatoRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        candidatoService.findByEmailEntity(emailEsperado);
//    }
//
//    @Test
//    public void deveBuscarCandidatoPeloNomeCorretamente() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//        final String emailEsperado = "heloise.lopes@dbccompany.com.br";
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findByNomeCompleto(anyString())).thenReturn(Optional.of(candidatoEntity));
//        CandidatoEntity candidato = candidatoService.findByNome(nomeEsperado);
//
//        assertEquals(nomeEsperado, candidato.getNomeCompleto());
//        assertEquals(emailEsperado, candidato.getEmail());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoNomeNaoEstiverCadastradoNoBanco() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//
//        when(candidatoRepository.findByNomeCompleto(anyString())).thenReturn(Optional.empty());
//        candidatoService.findByNome(nomeEsperado);
//    }
//
//    @Test
//    public void deveListarRelatorioCorretamente() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//        final int elementosEsperado = 1;
//        final int pagina = 0;
//        final int tamanho = 10;
//        final String nomeTrilha = "BACKEND";
//
//        RelatorioCandidatoCadastroDTO relatorio = getRelatorioCandidatoCadastroDTO();
//        PageImpl<RelatorioCandidatoCadastroDTO> paginaPrincipalDTOPage =
//                new PageImpl<>(List.of(relatorio), PageRequest.of(pagina, tamanho), 1);
//
//        when(candidatoRepository.listRelatorioCandidatoCadastroDTO(anyString(), anyString(), any()))
//                .thenReturn(paginaPrincipalDTOPage);
//        PageDTO<RelatorioCandidatoCadastroDTO> relatorioPageDTO =
//                candidatoService.listRelatorioCandidatoCadastroDTO(nomeEsperado, pagina, tamanho, nomeTrilha);
//
//        assertEquals(pagina, relatorioPageDTO.getPagina());
//        assertEquals(tamanho, relatorioPageDTO.getTamanho());
//        assertEquals(elementosEsperado, relatorioPageDTO.getElementos().size());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoRelatorioForVazio() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//        final int pagina = 0;
//        final int tamanho = 10;
//        final String nomeTrilha = "BACKEND";
//
//        PageImpl<RelatorioCandidatoCadastroDTO> paginaPrincipalDTOPage =
//                new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 1);
//
//        when(candidatoRepository.listRelatorioCandidatoCadastroDTO(anyString(), anyString(), any()))
//                .thenReturn(paginaPrincipalDTOPage);
//        PageDTO<RelatorioCandidatoCadastroDTO> relatorioPageDTO =
//                candidatoService.listRelatorioCandidatoCadastroDTO(nomeEsperado, pagina, tamanho, nomeTrilha);
//    }
//
//    @Test
//    public void deveListarRelatorioRelatorioCorretamente() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//        final int elementosEsperado = 1;
//        final int pagina = 0;
//        final int tamanho = 10;
//        final String nomeTrilha = "BACKEND";
//
//        RelatorioCandidatoPaginaPrincipalDTO relatorio = getRelatorioCandidatoPaginaPrincipalDTO();
//        PageImpl<RelatorioCandidatoPaginaPrincipalDTO> paginaPrincipalDTOPage =
//                new PageImpl<>(List.of(relatorio), PageRequest.of(pagina, tamanho), 1);
//
//        when(candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(anyString(), anyString(), any()))
//                .thenReturn(paginaPrincipalDTOPage);
//        PageDTO<RelatorioCandidatoPaginaPrincipalDTO> relatorioPageDTO =
//                candidatoService.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeEsperado, pagina, tamanho, nomeTrilha);
//
//        assertEquals(pagina, relatorioPageDTO.getPagina());
//        assertEquals(tamanho, relatorioPageDTO.getTamanho());
//        assertEquals(elementosEsperado, relatorioPageDTO.getElementos().size());
//    }
//
//    @Test(expected = RegraDeNegocioException.class)
//    public void deveRetornarUmaExcecaoQuandoRelatorioRelatorioForVazio() throws RegraDeNegocioException {
//        final String nomeEsperado = "Heloise Isabela Lopes";
//        final int pagina = 0;
//        final int tamanho = 10;
//        final String nomeTrilha = "BACKEND";
//
//        PageImpl<RelatorioCandidatoPaginaPrincipalDTO> paginaPrincipalDTOPage =
//                new PageImpl<>(List.of(), PageRequest.of(pagina, tamanho), 1);
//
//        when(candidatoRepository.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(anyString(), anyString(), any()))
//                .thenReturn(paginaPrincipalDTOPage);
//        PageDTO<RelatorioCandidatoPaginaPrincipalDTO> relatorioPageDTO =
//                candidatoService.listRelatorioRelatorioCandidatoPaginaPrincipalDTO(nomeEsperado, pagina, tamanho, nomeTrilha);
//    }
//
//    @Test
//    public void deveDeletarFisicamenteUmCandidato() throws RegraDeNegocioException {
//        CandidatoEntity candidatoEntity = getCandidatoEntity();
//
//        when(candidatoRepository.findById(any())).thenReturn(Optional.of(candidatoEntity));
//
//        candidatoService.deleteFisico(1);
//
//        verify(candidatoRepository).deleteById(any());
//    }
//
//    private static CandidatoEntity getCandidatoEntity() {
//        LinguagemEntity linguagemEntity = getLinguagemEntity();
//        List<LinguagemEntity> linguagemList = new ArrayList<>();
//        linguagemList.add(linguagemEntity);
//
//        CandidatoEntity candidatoEntity = new CandidatoEntity();
//        candidatoEntity.setIdCandidato(1);
//        candidatoEntity.setNotaProva(8.00);
//        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
//        candidatoEntity.setCidade("Santana");
//        candidatoEntity.setEstado("AP");
//        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
//        candidatoEntity.setGenero(Genero.FEMININO);
//        candidatoEntity.setLinguagens(new HashSet<>(linguagemList));
//        candidatoEntity.setEdicao(getEdicaoEntity());
//        candidatoEntity.setTrilha(getTrilhaEntity());
//        candidatoEntity.setAtivo('T');
//
//        return candidatoEntity;
//    }
//
//    private static CandidatoCreateDTO getCandidatoCreateDTO() {
//        LinguagemDTO linguagemDTO = new LinguagemDTO("Java");
//        List<LinguagemDTO> linguagemDTOList = new ArrayList<>();
//        linguagemDTOList.add(linguagemDTO);
//
//        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
//        candidatoCreateDTO.setNomeCompleto("Heloise Isabela Lopes");
//        candidatoCreateDTO.setCidade("Santana");
//        candidatoCreateDTO.setEstado("AP");
//        candidatoCreateDTO.setEmail("heloise.lopes@dbccompany.com.br");
//        candidatoCreateDTO.setLinguagens(new HashSet<>(linguagemDTOList));
//        candidatoCreateDTO.setTrilha(getTrilhaDTO());
//        candidatoCreateDTO.setEdicao(getEdicaoDTO());
////        candidatoCreateDTO.setGenero(Genero.FEMININO);
//        candidatoCreateDTO.setAtivo('T');
//
//        return candidatoCreateDTO;
//    }
//
//    private static TrilhaDTO getTrilhaDTO() {
//        return new TrilhaDTO("BACKEND");
//    }
//
//    private static EdicaoDTO getEdicaoDTO() {
//        return new EdicaoDTO("Edição 10");
//    }
//
//    private static LinguagemEntity getLinguagemEntity() {
//        LinguagemEntity linguagemEntity = new LinguagemEntity();
//        linguagemEntity.setIdLinguagem(1);
//        linguagemEntity.setNome("Java");
//
//        return linguagemEntity;
//    }
//
//    private static TrilhaEntity getTrilhaEntity() {
//        TrilhaEntity trilha = new TrilhaEntity();
//        trilha.setIdTrilha(1);
//        trilha.setNome("BACKEND");
//
//        return trilha;
//    }
//
//    private static EdicaoEntity getEdicaoEntity() {
//        EdicaoEntity edicao = new EdicaoEntity();
//        edicao.setIdEdicao(1);
//        edicao.setNome("Edição 10");
//
//        return edicao;
//    }
//
//    private static RelatorioCandidatoCadastroDTO getRelatorioCandidatoCadastroDTO() {
//        return new RelatorioCandidatoCadastroDTO(1,
//                "Heloise Isabela Lopes",
//                "heloise.lopes@dbccompany.com.br",
//                8.25,
//                "BACKEND",
//                "Edição 10",
//                Genero.FEMININO,
//                'T', "Santana", "AP", "Sem observação", null
//        );
//    }
//
//    private static RelatorioCandidatoPaginaPrincipalDTO getRelatorioCandidatoPaginaPrincipalDTO() {
//        return new RelatorioCandidatoPaginaPrincipalDTO(1,
//                "heloise.lopes@dbccompany.com.br",
//                "Heloise Isabela Lopes", 8.25, "BACKEND"
//        );
//    }
//}
