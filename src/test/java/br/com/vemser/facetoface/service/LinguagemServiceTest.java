package br.com.vemser.facetoface.service;

import br.com.vemser.facetoface.dto.EdicaoDTO;
import br.com.vemser.facetoface.dto.LinguagemDTO;
import br.com.vemser.facetoface.dto.TrilhaDTO;
import br.com.vemser.facetoface.dto.candidato.CandidatoCreateDTO;
import br.com.vemser.facetoface.entity.CandidatoEntity;
import br.com.vemser.facetoface.entity.EdicaoEntity;
import br.com.vemser.facetoface.entity.LinguagemEntity;
import br.com.vemser.facetoface.entity.TrilhaEntity;
import br.com.vemser.facetoface.entity.enums.Genero;
import br.com.vemser.facetoface.exceptions.RegraDeNegocioException;
import br.com.vemser.facetoface.repository.LinguagemRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LinguagemServiceTest {

    @InjectMocks
    private LinguagemService linguagemService;

    @Mock
    private LinguagemRepository linguagemRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(linguagemService, "objectMapper", objectMapper);
    }

    @Test
    public void testarBuscarLinguagemPeloNomeComSucesso(){
        //Setup
        final String NOME = "Java";
        final int id = 1;
        LinguagemEntity linguagemEntity = new LinguagemEntity();
        linguagemEntity.setNome(NOME);
        linguagemEntity.setIdLinguagem(id);

        //Act
        when(linguagemRepository.findByNome(anyString())).thenReturn(Optional.of(linguagemEntity));
        LinguagemEntity linguagemResponse = linguagemService.findByNome(NOME);

        //Assert
        assertEquals(NOME, linguagemResponse.getNome());
    }

    @Test
    public void deveCriarUmaNovaLinguagemCasoNaoExistaONomeNoBanco() {
        final String linguagemNome = "Java";
        LinguagemEntity linguagem = getLinguagemEntity();

        when(linguagemRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(linguagemRepository.save(any())).thenReturn(linguagem);

        LinguagemEntity linguagemRetornada = linguagemService.findByNome(linguagemNome);

        assertEquals(linguagemNome, linguagemRetornada.getNome());
    }

    @Test
    public void tentarConverterLinguagemDTOParaEntityComSucesso() {
        //Setup
        LinguagemDTO linguagemDTO = getLinguagemDTO();

        // Act

        LinguagemEntity linguagemResponse = linguagemService.converterEntity(linguagemDTO);
        //Assert
        assertEquals(linguagemResponse.getNome(), linguagemDTO.getNome());
    }

    @Test
    public void deveTestarDeleteFisicoComSucesso() throws RegraDeNegocioException {
        //Setup
        final int idLinguagem = 1;
        LinguagemEntity linguagem = getLinguagemEntity();

        // Act
        when(linguagemRepository.findById(idLinguagem)).thenReturn(Optional.of(linguagem));
        linguagemService.deleteFisico(linguagem.getIdLinguagem());

        //Assert
        verify(linguagemRepository).deleteById(idLinguagem);

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarBuscaLinguagemPorIdComErro() throws RegraDeNegocioException {
        final int id = 1;
        when(linguagemRepository.findById(id)).thenReturn(Optional.empty());
        linguagemService.findById(id);
    }

    @Test
    public void deveTestarCriacaoComSucesso() {
        // Setup
        LinguagemDTO linguagemDTO = getLinguagemDTO();
        LinguagemEntity linguagem = getLinguagemEntity();
        Set<CandidatoEntity> lista = new HashSet<>();
        lista.add(getCandidatoEntity());
        linguagem.setCandidatoEntities(lista);
        // Act
        when(linguagemRepository.save(any())).thenReturn(linguagem);
        LinguagemEntity linguagemDTOResponse = linguagemService.create(linguagemDTO);
        // Assert
        assertEquals(1, linguagemDTOResponse.getIdLinguagem());
        assertEquals(linguagem.getNome(), linguagemDTOResponse.getNome());
        assertEquals(linguagem.getIdLinguagem(), linguagemDTOResponse.getIdLinguagem());
        assertEquals(1, linguagemDTOResponse.getCandidatoEntities().size());
    }

    @Test
    public void testarCriarLinguagemCasoNaoExistaUmaComSucesso() {
        //Setup
        final String nome = "Python";
        LinguagemDTO linguagemDTO = new LinguagemDTO();
        linguagemDTO.setNome(nome);
        final int id = 2;
        LinguagemEntity linguagem = new LinguagemEntity();
        linguagem.setNome(nome);
        linguagem.setIdLinguagem(id);
        //Act
        when(linguagemRepository.save(any())).thenReturn(linguagem);
        LinguagemEntity linguagemEntityResponse = linguagemService.create(linguagemDTO);
        //Assert
        assertEquals(2, linguagemEntityResponse.getIdLinguagem());
        assertEquals(linguagem.getNome(), linguagemEntityResponse.getNome());
        assertEquals(linguagem.getIdLinguagem(), linguagemEntityResponse.getIdLinguagem());
    }

    private static CandidatoEntity getCandidatoEntity() {
        LinguagemEntity linguagemEntity = getLinguagemEntity();
        Set<LinguagemEntity> linguagemList = new HashSet<>();
        linguagemList.add(linguagemEntity);

        CandidatoEntity candidatoEntity = new CandidatoEntity();
        candidatoEntity.setIdCandidato(1);
        candidatoEntity.setNotaProva(8.00);
        candidatoEntity.setNomeCompleto("Heloise Isabela Lopes");
        candidatoEntity.setCidade("Santana");
        candidatoEntity.setEstado("AP");
        candidatoEntity.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoEntity.setGenero(Genero.FEMININO);
        candidatoEntity.setLinguagens(linguagemList);
        candidatoEntity.setEdicao(getEdicaoEntity());
        candidatoEntity.setTrilha(getTrilhaEntity());
        candidatoEntity.setAtivo('T');

        return candidatoEntity;
    }

    private static Set<CandidatoEntity> getListaCandidato() {
        Set<CandidatoEntity> lista = new HashSet<>();
        lista.add(getCandidatoEntity());
        return lista;
    }

    private static CandidatoCreateDTO getCandidatoCreateDTO() {
        LinguagemDTO linguagemDTO = new LinguagemDTO("Java");
        Set<LinguagemDTO> linguagemDTOList = new HashSet<>();
        linguagemDTOList.add(linguagemDTO);

        CandidatoCreateDTO candidatoCreateDTO = new CandidatoCreateDTO();
        candidatoCreateDTO.setNomeCompleto("Heloise Isabela Lopes");
        candidatoCreateDTO.setCidade("Santana");
        candidatoCreateDTO.setEstado("AP");
        candidatoCreateDTO.setEmail("heloise.lopes@dbccompany.com.br");
        candidatoCreateDTO.setLinguagens(linguagemDTOList);
        candidatoCreateDTO.setTrilha(getTrilhaDTO());
        candidatoCreateDTO.setEdicao(getEdicaoDTO());
//        candidatoCreateDTO.setGenero(Genero.FEMININO);
        candidatoCreateDTO.setAtivo('T');

        return candidatoCreateDTO;
    }

    private static TrilhaDTO getTrilhaDTO() {
        return new TrilhaDTO("BACKEND");
    }

    private static EdicaoDTO getEdicaoDTO() {
        return new EdicaoDTO("Edição 10");
    }

    private static LinguagemEntity getLinguagemEntity() {
        LinguagemEntity linguagemEntity = new LinguagemEntity();
        linguagemEntity.setIdLinguagem(1);
        linguagemEntity.setNome("Java");

        return linguagemEntity;
    }

    private static TrilhaEntity getTrilhaEntity() {
        TrilhaEntity trilha = new TrilhaEntity();
        trilha.setIdTrilha(1);
        trilha.setNome("BACKEND");

        return trilha;
    }

    private static EdicaoEntity getEdicaoEntity() {
        EdicaoEntity edicao = new EdicaoEntity();
        edicao.setIdEdicao(1);
        edicao.setNome("Edição 10");

        return edicao;
    }

    private static LinguagemDTO getLinguagemDTO() {
        LinguagemDTO linguagemDTO = new LinguagemDTO();
        linguagemDTO.setNome("Java");
        return linguagemDTO;
    }

}
