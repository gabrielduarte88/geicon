package com.geicon.blue.tasks.workers;

import com.geicon.blue.api.models.EnvioEmail;
import com.geicon.blue.api.models.Tarefa;
import com.geicon.blue.api.models.enums.TarefaStatus;
import com.geicon.blue.framework.util.Mail;
import com.geicon.blue.framework.validation.Validator;
import com.geicon.blue.tasks.services.api.EnvioEmailService;
import com.geicon.blue.tasks.services.api.TarefaService;
import com.geicon.blue.tasks.validators.EnvioEmailValidator;
import java.util.Date;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Executor de tarefa - Envio de e-mail
 *
 * @author Gabriel Duarte
 */
@RequestScoped
public class EnvioEmailWorker implements Runnable {
    /**
     * Tarefa
     */
    private Tarefa tarefa;
    /**
     * Serviços de tarefas
     */
    private TarefaService service;
    /**
     * Serviços de envio de e-mail
     */
    private EnvioEmailService envioEmailService;
    /**
     * Mailer
     */
    @Inject
    private Mail mail;
    /**
     * Logger
     */
    private final Logger logger = LogManager.getLogger();

    /**
     * Construtor
     *
     * @param tarefa Tarefa
     * @param service Serviços de tarefas
     * @param envioEmailService Serviços de envio de e-mail
     */
    public EnvioEmailWorker(Tarefa tarefa, TarefaService service, EnvioEmailService envioEmailService) {
        this.tarefa = tarefa;
        this.service = service;
        this.envioEmailService = envioEmailService;
    }

    /**
     * Inicializacão
     *
     * @param tarefa Tarefa
     */
    public void init(Tarefa tarefa) {
        if (tarefa == null) {
            throw new IllegalArgumentException();
        }

        this.tarefa = tarefa;
    }

    @Override
    public void run() {
        try {
            //Assinalando o inicio da tarefa
            tarefa.setInicio(new Date());
            tarefa.setStatus(TarefaStatus.EM_EXECUCAO);
            service.update(tarefa);

            if (tarefa.getParametros() != null && !tarefa.getParametros().isEmpty()) {
                EnvioEmail envioEmail = envioEmailService.buscarEnvioPorId(Integer.parseInt(tarefa.getParametros()));

                if (envioEmail != null) {
                    Validator val = new EnvioEmailValidator(envioEmail);

                    if (!val.hasErrors()) {
                        mail.send(envioEmail.getEmailDestino(), envioEmail.getEmailDestinoCC(), envioEmail.getEmailDestinoCCO(), envioEmail.getAssunto(), envioEmail.getTexto());

                        envioEmail.setDataEnvio(new Date());

                        envioEmailService.update(envioEmail);

                        //Assinalando o final da tarefa
                        tarefa.setStatus(TarefaStatus.SUCESSO);
                        tarefa.setTermino(new Date());
                        service.update(tarefa);
                    }
                    else {
                        StringBuilder erros = new StringBuilder(0);

                        erros.append("Os seguintes erros foram encontrados:\n");

                        for (String erro : val.getErrors()) {
                            erros.append(erro).append("\n");
                        }

                        //Assinalando erro na tarefa
                        tarefa.setStatus(TarefaStatus.ERRO);
                        tarefa.setTermino(new Date());
                        tarefa.setMensagem(erros.toString());
                        service.update(tarefa);
                    }
                }
                else {
                    //Assinalando erro na tarefa
                    tarefa.setStatus(TarefaStatus.ERRO);
                    tarefa.setTermino(new Date());
                    tarefa.setMensagem("O envio de e-mail indicado não existe.");
                    service.update(tarefa);
                }
            }
            else {
                //Assinalando erro na tarefa
                tarefa.setStatus(TarefaStatus.ERRO);
                tarefa.setTermino(new Date());
                tarefa.setMensagem("Nao foi indicado o código do envio de e-mail a ser executado.");
                service.update(tarefa);
            }

        }
        catch (Exception ex) {
            logger.error("Houve um erro no envio do e-mail.", ex);

            //Assinalando erro na tarefa
            tarefa.setStatus(TarefaStatus.ERRO);
            tarefa.setTermino(new Date());
            tarefa.setMensagem(ex.getMessage());
            service.update(tarefa);
        }
    }
}
