package br.com.techagro.helpdesk.validation;

/**
 * Regra reutilizavel de validacao para um tipo de modelo.
 * Implementacoes registradas como beans podem ser injetadas em diferentes
 * servicos por meio de uma List<IModelValidation<T>> do tipo desejado.
 *
 * @param <T> tipo do modelo validado
 */
@FunctionalInterface
public interface IModelValidation<T> {

    void validate(T model);
}
