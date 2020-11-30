package com.exemplo.sping.userCRUD.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.exemplo.sping.userCRUD.config.LocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable{ 
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id", columnDefinition="SERIAL")
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Nome completo é obrigatório")
	private String name;

	@NotEmpty(message = "Cpf é obrigatório")
	@CPF(message = "CPF inválido")
	private String cpf;
	
	@NotNull(message = "Data de nascimento é obrigatório")
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate birthDate;
	
	private String password;
}
