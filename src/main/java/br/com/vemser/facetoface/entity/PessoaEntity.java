//package br.com.vemser.facetoface.entity;
//
//import br.com.vemser.facetoface.entity.enums.Genero;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.*;
//
//import javax.persistence.*;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@MappedSuperclass
//@Inheritance
//public abstract class PessoaEntity {
//
////    @Id
//////    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARTAO_CREDITO")
//////    @SequenceGenerator(name = "SEQ_CARTAO_CREDITO", sequenceName = "seq_cartao_credito", allocationSize = 1)
////    @Column(name = "id_pessoa")
////    private Integer idPessoa;
//
//    @Column(name = "nome_completo")
//    private String nomeCompleto;
//
//    @Column(name = "cidade")
//    private String cidade;
//
//    @Column(name = "estado")
//    private String estado;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "id_genero")
//    @Enumerated(EnumType.ORDINAL)
//    private Genero genero;
//
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_trilha", referencedColumnName = "id_trilha")
//    private TrilhaEntity trilha;
//
//    @Column(name = "ativo")
//    private char ativo;
//}
