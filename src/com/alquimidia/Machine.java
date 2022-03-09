package com.alquimidia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.alquimidia.dao.DAOUsuarios;
import com.alquimidia.dao.DAOUsuariosBio;
import com.alquimidia.easyInner.entity.Inner;
import com.alquimidia.entity.Horarios;
import com.alquimidia.entity.UsuarioSemDigital;
import com.alquimidia.entity.Usuarios;
import com.alquimidia.enumeradores.Enumeradores;
import com.alquimidia.utils.EasyInnerUtils;
import com.topdata.BioService;
import com.topdata.EasyInner;

public class Machine {
	private HashMap<Integer, Inner> listInners;
	private boolean stop;
	Long initConnect;

	public Machine() {
		listInners = new HashMap<Integer, Inner>();
		stop = false;
	}

	public void addInner(Inner inner) {
		listInners.put(inner.Numero, inner);
	}

	public void startMachine() throws InterruptedException {
		Inner inner = new Inner();
		Integer ret = 0;
		EasyInner.FecharPortaComunicacao();
		EasyInner.DefinirTipoConexao(2);
		ret = EasyInner.AbrirPortaComunicacao(3570);
		if (ret == EasyInner.RET_COMANDO_OK) {
			System.out.println("Porta Aberta");
			// CONFIGURA��ES QUE PRECISAM CARREGAR DE UM ARQUIVO
			inner.PadraoCartao = 1;
			inner.Numero = 1;
			inner.QtdDigitos = 14;
			inner.TipoLeitor = 4;
			inner.Porta = 3570;
			inner.Acionamento = 8;
			inner.TipoConexao = 2;
			inner.EstadoTeclado = Enumeradores.EstadosTeclado.TECLADO_EM_BRANCO;
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
			inner.DoisLeitores = true;
			inner.Catraca = true;
			inner.Biometrico = true;
			inner.Teclado = true;
			inner.Master = "0";
			// CONFIGURA��ES QUE PRECISAM CARREGAR DE UM ARQUIVO
			addInner(inner);
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunica��o.");
		}
	}

	// INICIAR COMUNICA��O COM A CATRACA
	private void machine() throws InterruptedException {
		while (!stop) {
			for (Object objInner : listInners.values()) {
				Inner inner = (Inner) objInner;
				System.out.println("57 " + inner.EstadoAtual);
				switch (inner.EstadoAtual) {
				// PASSO_ESTADO_CONECTAR
				case ESTADO_CONECTAR:
					STEP_STATE_CONNECT(inner);
					break;
				// PASSO_ESTADO_RECONECTAR
				case ESTADO_RECONECTAR:
					STEP_STATE_RECONNECT(inner);
					break;
				// ESTADO_RECEBER_FIRWARE
				case ESTADO_RECEBER_FIRWARE:
					STATE_RECEIVE_FIRWARE(inner);
					break;
				// ESTADO_RECEBER_MODELO_BIO
				case ESTADO_RECEBER_MODELO_BIO:
					PASS_STATE_RECEBER_MODELO_BIO(inner);
					break;
				// ESTADO_RECEBER_VERSAO_BIO
				case ESTADO_RECEBER_VERSAO_BIO:
					PASS_STATE_RECEBER_VERSAO_BIO(inner);
					break;
				// ESTADO_ENVIAR_CFG_OFFLINE
				case ESTADO_ENVIAR_CFG_OFFLINE:
					STEP_STATE_ENVIAR_CFG_OFFLINE(inner);
					break;
				// ESTADO_ENVIAR_LISTA_OFFLINE
				case ESTADO_ENVIAR_LISTA_OFFLINE:
					PASS_STATE_ENVIAR_LISTA_OFFLINE(inner);
					break;
				// ESTADO_ENVIAR_LISTA_SEMDIGITAL
				case ESTADO_ENVIAR_LISTA_SEMDIGITAL:
					STEP_STATE_ENVIAR_LISTA_SEMDIGITAL(inner);
					break;
				// ESTADO_RECEBER_QTD_BILHETES_OFF
				case ESTADO_RECEBER_QTD_BILHETES_OFF:
					STEP_STATE_RECEBER_QTD_BILHETES_OFF(inner);
					break;
				// PASSO_ESTADO_ENVIAR_MSG_OFFLINE_CATRACA
				case ESTADO_ENVIAR_MSG_OFFLINE_CATRACA:
					STEP_STATUS_SEND_MSG_OFFLINE_RATCHET(inner);
					break;
				// ESTADO_ENVIAR_DATA_HORA
				case ESTADO_ENVIAR_DATA_HORA:
					STEP_STATE_SEND_DATE_TIME(inner);
					break;
				// ESTADO_ENVIAR_CFG_ONLINE
				case ESTADO_ENVIAR_CFG_ONLINE:
					STEP_STATE_ENVIAR_CFG_ONLINE(inner);
					break;
				// ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE
				case ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE:
					STEP_STATUS_SEND_CONFIGMUD_ONLINE_OFFLINE(inner);
					break;
				// ESTADO_ENVIAR_MSG_PADRAO
				case ESTADO_ENVIAR_MSG_PADRAO:
					STEP_STATUS_SEND_STANDARD_MSG(inner);
					break;
				// ESTADO_CONFIGURAR_ENTRADAS_ONLINE
				case ESTADO_CONFIGURAR_ENTRADAS_ONLINE:
					STAGE_STEP_CONFIGURE_ONLINE_ENTRIES(inner);
					break;
				// ESTADO_POLLING
				case ESTADO_POLLING:
					STEP_POLLING_STATE(inner);
					break;
				default:
					break;
				}
			}
			Thread.sleep(1);
		}
	}

	// PASSO_ESTADO_POLLING
	private void STEP_POLLING_STATE(Inner inner) {
		try {
			Integer ret = 0;
			StringBuffer card = new StringBuffer();
			// EXIBE ESTADO DO INNER NO RODAPE DA JANELA
			System.out.println("Inner " + inner.Numero + " Estado de Polling...");
			card.delete(0, card.length());
			ret = -1;
			// RECEBE DADOS DO CARTAO
			int[] iArrBcardRb = new int[8];
			// ENVIA O COMANDO DE COLETA DE BILHETES
			if (inner.TipoLeitor == Enumeradores.QRCODE) {
				ret = EasyInner.ReceberDadosOnLineComLetras(inner.Numero, iArrBcardRb, card);
			} else {
				ret = EasyInner.ReceberDadosOnLine(inner.Numero, iArrBcardRb, card);
			}
			// ATRIBUI TEMPORIZADOR
			inner.Temporizador = (int) System.currentTimeMillis();
			inner.TempoInicialMensagem = (int) System.currentTimeMillis();
			if (ret == Enumeradores.RET_COMANDO_OK) {
				if (iArrBcardRb[1] == Enumeradores.TECLA_FUNCAO || iArrBcardRb[1] == Enumeradores.TECLA_ANULA
						|| ((card.length() == 0)
								&& !(inner.EstadoTeclado == Enumeradores.EstadosTeclado.AGUARDANDO_TECLADO))) {
					inner.CountTentativasEnvioComando = 0;
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_AGUARDA_TEMPO_MENSAGEM;
					return;
				}
				/*******************************************************/
				//ATRIBUINDO OS DADOS DO CART�O AO INNER ATUAL
				inner.BilheteInner.Origem        = iArrBcardRb[0];
                inner.BilheteInner.Complemento   = iArrBcardRb[1];
                inner.BilheteInner.Dia           = iArrBcardRb[2];
                inner.BilheteInner.Mes           = iArrBcardRb[3];
                inner.BilheteInner.Ano           = iArrBcardRb[4];
                inner.BilheteInner.Hora          = iArrBcardRb[5];
                inner.BilheteInner.Minuto        = iArrBcardRb[6];
                inner.BilheteInner.Segundo       = iArrBcardRb[7];
                
                //LIMPANDO A VARIAVEL STRINGBUILDER E ATRIBUINDO UM NOVO VALOR
                inner.BilheteInner.Cartao.setLength(0);
                inner.BilheteInner.Cartao = new StringBuilder(card.toString());
                
                System.out.println("194 " + inner.BilheteInner.Cartao);
                
                //PAREI AKI
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// PASSO_ESTADO_CONFIGURAR_ENTRADAS_ONLINE
	private void STAGE_STEP_CONFIGURE_ONLINE_ENTRIES(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER DO RODAP� DA JANELA
			System.out.println("Inner " + inner.Numero + " Configurando Entradas Online...");
			// DECLARA��O DE VARIAVEIS
			ret = Enumeradores.Limpar;
			// CONVERTER BINARIO PARA DECIMAL ---> VER NO MANUAL ANEXO III
			int decimalValue = configureEntriesChangeOnLine(inner);
			ret = EasyInner.EnviarFormasEntradasOnLine(inner.Numero, (byte) inner.QtdDigitos, 1, (byte) decimalValue,
					15, 17);
			// TESTA O RETORNO DO COMANDO
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// VAI PARA O ESTADO DE POLLING
				inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_POLLING;
			} else {
				// CASO ELE N�O CONSIGA, TENTARA ENVIAR TRES VEZES SE N�O CONSEGUIR VOLTA PARA O
				// PASSO ESTADO_RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				// ADICIONA 1 AO CONTADOR DE TENTANTIVAS
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_MSG_PADRAO
	private void STEP_STATUS_SEND_STANDARD_MSG(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER DO RODAP� DA JANELA
			System.out.println("Inner " + inner.Numero + " Enviando Mensagem Padr�o...");
			// DECLARA��O DE VARIAVEIS
			ret = Enumeradores.Limpar;
			// ENVIAR COMANDO DEFININDO A MENSAGEM PADR�O ONLINE PARA O INNER
			ret = EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 1, "   Modo Online");
			// TESTA O RETORNO DA MENSAGEM ENVIADA
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// MUDA O PASSO PARA CONFIGURA��O DE ENTRADA ONLINE
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONFIGURAR_ENTRADAS_ONLINE;
			} else {
				// CASO ELE N�O CONSIGA, TENTARA ENVIAR TRES VEZES, SE N�O CONEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				// ADICIONAR 1 AO CONTADOR DE TENTANTIVAS
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE
	private void STEP_STATUS_SEND_CONFIGMUD_ONLINE_OFFLINE(Inner inner) {
		try {
			Integer ret = 0;
			System.out.println("Inner " + inner.Numero + " Enviar mudancas on off...");
			// HABILITA/DESABILITA A MUDAN�A AUTOMATICA DO MODO OFFILINE DO INNER PARA
			// ONLINE E VICE-VERSA
			EasyInner.HabilitarMudancaOnLineOffLine(2, 10);
			// CONFIGURA O TECLADO PARA QUANDO O INNER VOLTAR PARA ONLINE APENAS UMA QUEDA
			// PARA OFFLINE
			EasyInner.DefinirConfiguracaoTecladoOnLine(inner.QtdDigitos, 1, 5, 17);
			// DEFINE MUDAN�AS ONLINE
			// FUN��O QUE CONFIGURA BIT A BIT, VER NO MANUAL ANEXO III
			EasyInner.DefinirEntradasMudancaOnLine(configureEntriesChangeOnLine(inner));
			if (inner.Biometrico) {
				// CONFIGRA ENTRADAS MUDAN�AS OFFLINE COM BIOMETRIA
				EasyInner.DefinirEntradasMudancaOffLineComBiometria(
						(inner.Teclado ? Enumeradores.Opcao_SIM : Enumeradores.Opcao_NAO), 3,
						(byte) (inner.DoisLeitores ? 3 : 0), inner.Verificacao, inner.Identificacao);
			} else {
				// CONFIGURA ENTRADAS MUDAN�A OFFLINE
				if (inner.CatInvertida) {
					EasyInner.DefinirEntradasMudancaOffLine(
							(inner.Teclado ? Enumeradores.Opcao_SIM : Enumeradores.Opcao_NAO),
							(byte) (inner.DoisLeitores ? 1 : 4), (byte) (inner.DoisLeitores ? 2 : 0), 0);
				} else {
					EasyInner.DefinirEntradasMudancaOffLine(
							(inner.Teclado ? Enumeradores.Opcao_SIM : Enumeradores.Opcao_NAO),
							(byte) (inner.DoisLeitores ? 1 : 3), (byte) (inner.DoisLeitores ? 2 : 0), 0);
				}
			}

			// DEFINE MENSAGEM DE ALTERA��O ONLINE -> OFFLINE
			EasyInner.DefinirMensagemPadraoMudancaOffLine(1, " Modo OffLine");
			// DEFINE MENSAGEM DE ALTERA��O OFFILINE -> ONLINE
			EasyInner.DefinirMensagemPadraoMudancaOnLine(1, "Modo Online");
			// ENVIA CONFIGURA��ES
			ret = EasyInner.EnviarConfiguracoesMudancaAutomaticaOnLineOffLine(inner.Numero);

			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_PADRAO;
				inner.TempoColeta = (int) System.currentTimeMillis() + 3000;
				inner.TentativasColeta = 0;
			} else {
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// ConfiguraEntradasMudancaOnLine
	private int configureEntriesChangeOnLine(Inner inner) {
		String settings;
		// HABILITAR TECLADO
		settings = (inner.Teclado ? "1" : "0");
		if (!inner.Biometrico) {
			// CODIGO DE BARRAS E PROXIMDIDADE
			// DOIS LEITORES
			if (inner.DoisLeitores) {
				// LEITOR 2 SAIDA + //LEITOR 1 ENTRADA
				settings = "010" + "001" + settings;
			} else {
				// APENAS UM LEITOR
				// LEITOR 2 DESATIVADO + //LEITOR 1 CONFIGURADO PARA ENTRADA E SAIDA
				settings = "000" + "011" + settings;
			}
			// HABILITADO
			settings = "1" + settings;
			/*
            --------------------------------------------------------------------------------------------------
            |       7        |     6      |   5    |   4    |   3    |    2    |      1     |        0       |
            --------------------------------------------------------------------------------------------------
            | SETA/RESETA    |  Bit 2     |  Bit 1 |  Bit 0 | Bit 2  |  Bit 1  |   Bit 0    |  TECLADO       |
            |   CONFIG.      | LEITOR 2   |        |        |        |         |            |                |
            |   bit-a-bit    |            |        |        |        |         |            |                |
            --------------------------------------------------------------------------------------------------
            | 1   HABILITA   | 000 - DESATIVAR LEITOR       |  000 - DESATIVAR LEITOR       | 1 HABILITA     |
            | 0   DESABILITA | 001 - LEITOR ENTRADA         |  001 - LEITOR ENTRADA         | 0 DESABILITA   |
            |                | 010 - LEITOR SAIDA           |  010 - LEITOR SAIDA           |                |
            |                | 011 - LEITOR ENTRADA E SAIDA |  011 - LEITOR ENTRADA E SAIDA |                |
            |                | 100 - LEITOR ENTRADA E SAIDA |  100 - LEITOR ENTRADA E SAIDA |                |
            |                |   INVERTIDO                  |   INVERTIDO                   |                |
            --------------------------------------------------------------------------------------------------
            */
		} else {
			// BIT FIXO + HABILITADO + INDENTIFICA��O + VERIFICACAO + BIT FIXO + HABILITAR
			// LEITOR + HABILITAR APENAS LEITOR
			settings = "0" + "1" + inner.Identificacao + inner.Verificacao + "0" + (inner.DoisLeitores ? "11" : "10")
					+ settings;

            /*
            ------------------------------------------------------------------------------------------------------------------------
            |    7     |       6       |       5       |       4       |      3       |       2      |      1       |      0       |
            ------------------------------------------------------------------------------------------------------------------------
            | Bit FIXO | SETA/RESETA   | IDENTIFICA��O |  VERIFICA��O  |   Bit FIXO   |   LEITOR 1   | LEITOR 2     |  TECLADO     |
            |   '0'    |    CONFIG.    |      BIO      |      BIO      |    CONFIG    |              |              |              |
            |          | bit-a-bit BIO |               |               |      L2      |              |              |              |
            |          |               |               |               |     '0'      |              |              |              |
            ------------------------------------------------------------------------------------------------------------------------
            |    0     |  1-HABILITA   | 1-HABILITA    | 1-HABILITA    | 1-HABILITA   | 1-HABILITA   | 1-HABILITA   | 1-HABILITA   |
            |          |  0-DESABILITA | 0-DESABILITA  | 0-DESABILITA  | 0-DESABILITA | 0-DESABILITA | 0-DESABILITA | 0-DESABILITA |
            ------------------------------------------------------------------------------------------------------------------------
            */
		}
		// CONVERTER BINARIO PARA DECIMAL
		return EasyInnerUtils.BinarioParaDecimal(settings);
	}

	// PASSO_ESTADO_ENVIAR_CFG_ONLINE
	private void STEP_STATE_ENVIAR_CFG_ONLINE(Inner inner) {
		try {
			System.out.println("Inner " + inner.Numero + " Enviar configura��o Online...");
			Integer ret = 0;
			// MONTAR CONFIGURA��O MODO ONLINE
			mountConfiguracaoInner(inner, Enumeradores.MODO_ON_LINE);
			// ENVIAR AS CONFIGURA��ES AO INNER ATUAL
			ret = EasyInner.EnviarConfiguracoes(inner.Numero);
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// CASO CONSIGA ENVIAR AS CONFIGURA��ES, PASSA PARA O PASSO ENVIAR DATA HORA
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE;
			} else {
				// CASO ELE N�O CONSIGA, TENTARA ENVIAR TR�S VEZES, SE N�O CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_DATA_HORA
	private void STEP_STATE_SEND_DATE_TIME(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER NO RODAP� DA JANELA
			System.out.println("Inner " + inner.Numero + " Enviando data e hora...");
			Date Data = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yy");
			int Ano = Integer.parseInt(formatter.format(Data));
			formatter = new SimpleDateFormat("MM");
			int Mes = Integer.parseInt(formatter.format(Data));
			formatter = new SimpleDateFormat("dd");
			int Dia = Integer.parseInt(formatter.format(Data));
			formatter = new SimpleDateFormat("HH");
			int Hora = Integer.parseInt(formatter.format(Data));
			formatter = new SimpleDateFormat("mm");
			int Minuto = Integer.parseInt(formatter.format(Data));
			formatter = new SimpleDateFormat("ss");
			int Segundo = Integer.parseInt(formatter.format(Data));
			// ENVIAR COMANDO DO RELOGIO AO INNER ATUAL
			// RELOGIO INNER RELOGIO INNER = NEW RELOGIO INNER();
			// RelogioInner relogioInner = new RelogioInner();
			ret = EasyInner.EnviarRelogio(inner.Numero, Dia, Mes, Ano, Hora, Minuto, Segundo);
			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_CFG_ONLINE;
			} else {
				// CASO ELE N�O CONSIGA, TENTARA, ENVIAR TRES VEZES, SE N�O CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_MSG_OFFLINE_CATRACA
	private void STEP_STATUS_SEND_MSG_OFFLINE_RATCHET(Inner inner) {
		try {
			Integer ret = 0;
			System.out.println("Inner " + inner.Numero + " Enviar mensagem off catraca...");
			if (!inner.CatInvertida || (inner.Acionamento == (int) Enumeradores.Acionamento_Catraca_Entrada_E_Saida
					&& inner.CatInvertida)) {
				// MENSAGEM ENTRADA E SAIDA OFFILEN LIBERADA
				EasyInner.DefinirMensagemEntradaOffLine(1, "Entrada liberada.");
				EasyInner.DefinirMensagemSaidaOffLine(1, "Saida liberada.");
			} else {
				// MENSAGEM ENTRADA E SAIDA OFFLINE LIBERADO
				EasyInner.DefinirMensagemEntradaOffLine(1, "Saida liberada.");
				EasyInner.DefinirMensagemSaidaOffLine(1, "Entrada liberada.");
			}

			EasyInner.DefinirMensagemPadraoOffLine(1, "Modo OffLine");
			ret = EasyInner.EnviarMensagensOffLine(inner.Numero);

			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_DATA_HORA;
				inner.TempoColeta = (int) System.currentTimeMillis() + 3000;
			} else {
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_RECEBER_QTD_BILHETES_OFF
	private void STEP_STATE_RECEBER_QTD_BILHETES_OFF(Inner inner) {
		Integer ret = 0;
		System.out.println("Inner " + inner.Numero + " Receber quantidade bilhetes off...");
		int[] receive = new int[2];
		ret = EasyInner.ReceberQuantidadeBilhetes(inner.Numero, receive);
		if (ret == Enumeradores.RET_COMANDO_OK) {
			inner.BilhetesAReceber = receive[0];
			if (inner.BilhetesAReceber > 0) {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_COLETAR_BILHETES;
			} else {
				if (inner.Catraca) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_OFFLINE_CATRACA;
				} else {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_OFFLINE_COLETOR;
				}
			}
		} else {
			if (inner.Tentativas++ > 3) {
				inner.Tentativas = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
			}
		}
	}

	// PASSO_ESTADO_ENVIAR_LISTA_SEMDIGITAL
	private void STEP_STATE_ENVIAR_LISTA_SEMDIGITAL(Inner inner) {
		try {
			Integer ret = 0;
			System.out.println("Inner " + inner.Numero + " Enviar lista sem digital...");
			if (inner.ListaBio) {
				// CHAMA ROTINA QUE MONTA O BUFFER DE CARTOES QUE N�O IRAO PRECISAR DA DIGITAL
				// MontarListaSemDigital
				assembleListWithoutDigital(inner.InnerNetAcesso);
				if (inner.InnerNetAcesso) {
					// ENVIA O BUFFER COM A LISTA DE USUARIOS SEM DIGITAL
					ret = EasyInner.EnviarListaUsuariosSemDigitalBioVariavel(inner.Numero, inner.QtdDigitos);
				} else {
					// ENVIA O BUFFER COM A LISTA DE USUARIOS SEM DIGITAL
					ret = EasyInner.EnviarListaUsuariosSemDigitalBio(inner.Numero);
				}
			}
			if (ret == Enumeradores.RET_COMANDO_OK) {
				if (inner.InnerNetAcesso) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_QTD_BILHETES_OFF;
				} else {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_COLETAR_BILHETES;
				}
			} else {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	// MontarListaSemDigital
	private void assembleListWithoutDigital(boolean innerNetAcesso) {
		DAOUsuariosBio AcessoBio = new DAOUsuariosBio();
		List<UsuarioSemDigital> USersSD = null;
		try {
			USersSD = AcessoBio.ConsultarUsuarioSemDigital();
		} catch (SQLException ex) {
			Logger.getLogger(Machine.class.getName()).log(Level.SEVERE, null, ex);
		}
		for (UsuarioSemDigital s : USersSD) {
			if (innerNetAcesso) {
				EasyInner.IncluirUsuarioSemDigitalBioInnerAcesso(s.getCartao());
			} else {
				EasyInner.IncluirUsuarioSemDigitalBio(s.getCartao());
			}
		}
	}

	// PASSO_ESTADO_ENVIAR_LISTA_OFFLINE
	private void PASS_STATE_ENVIAR_LISTA_OFFLINE(Inner inner) {
		try {
			Integer ret = 0;
			System.out.println("Inner " + inner.Numero + " Enviar lista Off...");
			if (inner.Lista) {
				// MontarHorarios
				setSchedules();
				EasyInner.EnviarHorariosAcesso(inner.Numero);
				// DEFINE A LISTA DE VERIFICA��O
				if (inner.PadraoCartao == 1) {
					// MontarListaPadraoLivre(inner.QtdDigitos);
					assembleStandardListFree(inner.QtdDigitos);
				} else {
					// MontarListaPadraoTopdata(inner.QtdDigitos);
					assembleStandardTopdataList(inner.QtdDigitos);
				}
				// DEFINE QUAL TIPO DE LISTA(CONTROLE) DE ACESSO O INNER VAI UTILIZAR
				// UTILIZAR LISTA BRANCA (CART�ES FORA DA LISTA TEM O ACESSO NEGADO)
				EasyInner.DefinirTipoListaAcesso(1);
				ret = EasyInner.EnviarListaAcesso(inner.Numero);
			} else {
				// N�O UTILIZAR A LISTA DE ACESSO
				EasyInner.DefinirTipoListaAcesso(0);
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_SEMDIGITAL;
			}
			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_SEMDIGITAL;
			} else {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
			}
		} catch (IOException | ClassNotFoundException | SQLException ex) {
			System.out.println("ex" + ex);
		}

	}

	// MontarListaPadraoTopdata
	private void assembleStandardTopdataList(int qtdDigitos) {
		EasyInner.DefinirPadraoCartao(0);
		EasyInner.DefinirQuantidadeDigitosCartao(qtdDigitos);
		for (int i = 0; i < 5; i++) {
			EasyInner.InserirUsuarioListaAcesso(Integer.toString(i), 101);
		}
	}

	// MontarListaPadraoLivre
	private void assembleStandardListFree(int qtdDigitos) {
		EasyInner.DefinirPadraoCartao(1);
		EasyInner.DefinirQuantidadeDigitosCartao(qtdDigitos);

		try {
			List<Usuarios> listcard = DAOUsuarios.consultarUsuarios(0);
			for (int i = 0; listcard.size() > i; i++) {
				EasyInner.InserirUsuarioListaAcesso(listcard.get(i).getUsuario(), 101);
			}
		} catch (IOException | SQLException | ClassNotFoundException e) {
			System.out.println(e);
		}

	}

	private void setSchedules() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
		// INSERE NO BUFFER DA DLL HORARIO DE ACESSO
		byte bTime;
		byte bDay;
		byte bRange;
		byte bHour;
		byte bMin;

		List<Horarios> ListTimes = Horarios.MontarListaHorarios();

		for (int index = 0; index < ListTimes.size(); index++) {
			EasyInner.InserirHorarioAcesso(ListTimes.get(index).horario, ListTimes.get(index).dia,
					ListTimes.get(index).faixa, ListTimes.get(index).hora, ListTimes.get(index).minuto);
		}

	}

	// ESTADO_ENVIAR_CFG_OFFLINE
	private void STEP_STATE_ENVIAR_CFG_OFFLINE(Inner inner) {
		try {
			Integer ret = 0;
			// MENSAGENS DE STATUS
			System.out.println("Inner " + inner.Numero + " Enviado configura��es OFF-LINE...");
			// PREENCHE OS CAMPOS DE CONFIGURA��ES DO INNER
			// MontaConfiguracaoInner
			mountConfiguracaoInner(inner, Enumeradores.MODO_OFF_LINE);
			// ENVIA O COMANDO DE CONFIGURA��O
			ret = EasyInner.EnviarConfiguracoes(inner.Numero);
			// TESTA O RETORNO DO ENVIO DAS CONFIGURA��ES OFF LINE
			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.CountTentativasEnvioComando = 0;
				// VERIFICA SE O ENVIAR LISTA ESTA SELECIONADO
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_OFFLINE;
				inner.TempoColeta = (int) System.currentTimeMillis() + 3000;
			} else {
				// CASO ELE N�O CONSIGA, TENTARA, ENVIAR TR�S VEZES, SE N�O CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			System.out.println("120" + e.getMessage());
		}

	}

	// MONTAR CONFIGURA��O INNER
	// MontaConfiguracaoInner
	private void mountConfiguracaoInner(Inner inner, int modoOffLine) {
		try {
			// ANTES DE REAILIZAR A CONFIGURA��O PRECISA DEFINIR O PADR�O DA CART�O
			if (inner.PadraoCartao == 1) {
				EasyInner.DefinirPadraoCartao(Enumeradores.PADRAO_LIVRE);
			} else {
				EasyInner.DefinirPadraoCartao(Enumeradores.PADRAO_TOPDATA);
			}
			// DEFINE MODO DE COMUNICA��O
			if (modoOffLine == Enumeradores.MODO_OFF_LINE) {
				// CONFIGURA��O PARA MODO OFFLINE
				// PREPARA O INNER PARA TRABALHAR NO MODO OFF LINE.
				// AINDA N�O ENVIA ESSA INFORMA��O PARA O EQUIPAMENTO.
				EasyInner.ConfigurarInnerOffLine();
			} else {
				// CONFIGURA��O PARA MODO ONLINE
				// PREPARA O INNER PARA TRABALHAR NO MODO ON-LINE
				// AINDA N�O ENVIA ESSA INFORMA��O PARA O EQUIPAMENTO.
				EasyInner.ConfigurarInnerOnLine();
			}
			// VERIFICAR
			// ACIONAMENTOS 1 E 2
			// CONFIGURA COMO IRA O FUNCIONAR O ACIONAMENTO(RELE) 1 E 2 DO INNER, E POR
			// QUANTO TEMPO SER� ACIONADO
			System.out.println("inner.Acionamento " + inner.Acionamento);
			switch (inner.Acionamento) {
			// COLETOR
			case Enumeradores.Acionamento_Coletor:
				EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA_OU_SAIDA, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.ACIONA_REGISTRO_ENTRADA_OU_SAIDA, 5);
				break;
			// CATRACA
			case Enumeradores.Acionamento_Catraca_Entrada_E_Saida:
				EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA_OU_SAIDA, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;
			case Enumeradores.Acionamento_Catraca_Entrada:
				if (inner.CatInvertida) {
					EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_SAIDA, 5);
				} else {
					EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA, 5);
				}
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;

			case Enumeradores.Acionamento_Catraca_Saida:
				if (inner.CatInvertida) {
					EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA, 5);
				} else {
					EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_SAIDA, 5);
				}
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;

			case Enumeradores.Acionamento_Catraca_Urna:
				EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA_OU_SAIDA, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.ACIONA_REGISTRO_SAIDA, 5);
				break;

			case Enumeradores.Acionamento_Catraca_Saida_Liberada:
				// SE ESQUERDA SELECIONADO - INVERTE CODIGO
				if (inner.CatInvertida && (!inner.InnerNetAcesso)) {
					EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_ENTRADA_LIBERADA, 5);
				} else {
					EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_SAIDA_LIBERADA, 5);
				}
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;

			case Enumeradores.Acionamento_Catraca_Entrada_Liberada:
				// SE ESQUERDA SELECIONADO - INVERTE CODIGO
				if (inner.CatInvertida && (!inner.InnerNetAcesso)) {
					EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_SAIDA_LIBERADA, 5);
				} else {
					EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_ENTRADA_LIBERADA, 5);
				}
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;

			case Enumeradores.Acionamento_Catraca_Liberada_2_Sentidos:
				EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_LIBERADA_DOIS_SENTIDOS, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;

			case Enumeradores.Acionamento_Catraca_Sentido_Giro:
				EasyInner.ConfigurarAcionamento1(Enumeradores.CATRACA_LIBERADA_DOIS_SENTIDOS_MARCACAO_REGISTRO, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.NAO_UTILIZADO, 0);
				break;
			}
			selectReaderType(inner);
			EasyInner.DefinirQuantidadeDigitosCartao(inner.QtdDigitos);
			// HABILITAR O TECLADO
			EasyInner.HabilitarTeclado((inner.Teclado ? Enumeradores.Opcao_SIM : Enumeradores.Opcao_NAO), 0);
			// DEFINE OS VALORES PARA CONFIGURAR OS LEITORES DE ACORDO COM O TIPO DE INNER
			// DefineValoresParaConfigurarLeitores
			setsValuesConfigureReaders(inner);
			EasyInner.ConfigurarLeitor1(inner.ValorLeitor1);
			EasyInner.ConfigurarLeitor2(inner.ValorLeitor2);
			// BOX = CONFIGURA EQUIPAMENTOS COM DOIS LEITORES
			if (inner.DoisLeitores) {
				// EXIBE MENSAGENS DO SEGUNDO LEITOR
				EasyInner.ConfigurarWiegandDoisLeitores(1, Enumeradores.Opcao_SIM);
			}
			// REGISTRA ACESSO NEGADO
			EasyInner.RegistrarAcessoNegado(1);
			// CATRACA
			// DEFINE QUAL SERA O TIPO DE REGISTRO REALIZADO PELO INNER AO APROXIMAR UM
			// CART�O DO TIPO PROXIMIDADE NO LEITOR DO INNER, SEM QUE O USUARIO TENHA
			// PRESSIONADO A TECLA ENTRADA, SAIDA OU ENTRADA
			if ((inner.Acionamento == Enumeradores.Acionamento_Catraca_Entrada_E_Saida)
					|| (inner.Acionamento == Enumeradores.Acionamento_Catraca_Liberada_2_Sentidos)
					|| (inner.Acionamento == Enumeradores.Acionamento_Catraca_Sentido_Giro)) {
				// 12 LIBERA A CATRACA NOS DOIS SENTIDOS E REGISTRA O BILHETE CONFORME O SENTIDO
				// GIRO
				EasyInner.DefinirFuncaoDefaultLeitoresProximidade(12);
			} else {
				if ((inner.Acionamento == Enumeradores.Acionamento_Catraca_Entrada)
						|| (inner.Acionamento == Enumeradores.Acionamento_Catraca_Saida_Liberada)) {
					if (inner.CatInvertida == false) {
						// 10 REGISTRAR SEMPRE COMO ENTRADA
						EasyInner.DefinirFuncaoDefaultLeitoresProximidade(10);
					} else {
						// 11 REGISTRAR SEMPRE COMO SAIDA
						EasyInner.DefinirFuncaoDefaultLeitoresProximidade(11);
					}
				} else {
					if (inner.CatInvertida == false) {
						// 11 REGISTRAR SEMPRE COMO SAIDA
						EasyInner.DefinirFuncaoDefaultLeitoresProximidade(11);
					} else {
						// 10 REGISTRAR SEMPRE COMO ENTRADA
						EasyInner.DefinirFuncaoDefaultLeitoresProximidade(10);
					}
				}
			}
			EasyInner.DefinirNumeroCartaoMaster(inner.Master);
			// CONFIGURA O TIPO DE REGISTRO QUE SER� ASSOCIADO A UMA MARCA��O
			if (inner.Biometrico) {
				EasyInner.DefinirFuncaoDefaultSensorBiometria(10);
			} else {
				EasyInner.DefinirFuncaoDefaultSensorBiometria(0);
			}
			// CONFIGURA PARA RECEBER O HORARIO DOS DADOS QUANDO TIVER ONLINE
			if (inner.QtdDigitos <= 14) {
				EasyInner.ReceberDataHoraDadosOnLine(Enumeradores.Opcao_SIM);
			}
			if (inner.TipoLeitor == Enumeradores.BARRAS_PROX_QRCODE) {
				EasyInner.InserirQuantidadeDigitoVariavel(4);
				EasyInner.InserirQuantidadeDigitoVariavel(6);
				EasyInner.InserirQuantidadeDigitoVariavel(8);
				EasyInner.InserirQuantidadeDigitoVariavel(10);
				EasyInner.InserirQuantidadeDigitoVariavel(12);
				EasyInner.InserirQuantidadeDigitoVariavel(14);
				// EasyInner.InserirQuantidadeDigitoVariavel(16);
			}
			// CASO DESEJAR CONFIGURAR O INNER PARA LER CART�ES QUE POSSAM VARIAR DE 1
			// DIGITO A 16 DIGITOS
			// UTILIZAR A FUNCAO InserirQuantidadeDigitoVariavel
			// EasyInner.InserirQuantidadeDigitoVariavel(8);
			// EasyInner.InserirQuantidadeDigitoVariavel(10);
			// EasyInner.InserirQuantidadeDigitoVariavel(14);
			if (inner.VersaoFW.getFirwareSup() >= 5) {
				EasyInner.ConfigurarBioVariavel(1);
			}

		} catch (Exception e) {
			System.out.println("133" + e.getMessage());
		}
	}

	// DEFINE VALORES PARA CONFIGURAR O LEITORES
	private void setsValuesConfigureReaders(Inner inner) {
		// CONFIGURA��O CATRACA ESQUERDA OU DIREITA
		// DEFINE OS VALORES PARA CONFIGURAR OS LEITORES DE ACORDO COM O TIPO DE INNER
		if (inner.DoisLeitores) {
			if (inner.CatInvertida == false) {
				// DIREITA SELECIONADO
				inner.ValorLeitor1 = Enumeradores.SOMENTE_ENTRADA;
				inner.ValorLeitor2 = Enumeradores.SOMENTE_SAIDA;
			} else {
				// ESQUERDA SELECIONADO
				inner.ValorLeitor1 = Enumeradores.SOMENTE_SAIDA;
				inner.ValorLeitor2 = Enumeradores.SOMENTE_ENTRADA;
			}
		} else {
			if (inner.CatInvertida == false) {
				// DIREITA SELECIONADO
				inner.ValorLeitor1 = Enumeradores.ENTRADA_E_SAIDA;
			} else {
				// ESQUERDA SELECIONADO
				inner.ValorLeitor1 = Enumeradores.ENTRADA_E_SAIDA_INVERTIDAS;
			}

			inner.ValorLeitor2 = Enumeradores.DESATIVADO;
		}

	}

	// CONFIGURAR O TIPO DE LEITOR
	private void selectReaderType(Inner inner) {
		// CONFIGURAR O TIPO DE LEITOR
		switch (inner.TipoLeitor) {
		case Enumeradores.CODIGO_DE_BARRAS:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.CODIGO_DE_BARRAS);
			break;
		case Enumeradores.MAGNETICO:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.MAGNETICO);
			break;
		case Enumeradores.PROXIMIDADE_ABATRACK2:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.PROXIMIDADE_ABATRACK2);
			break;
		case Enumeradores.WIEGAND:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.WIEGAND);
			break;
		case Enumeradores.PROXIMIDADE_SMART_CARD:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.PROXIMIDADE_SMART_CARD);
			break;
		case Enumeradores.CODIGO_BARRAS_SERIAL:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.CODIGO_BARRAS_SERIAL);
			break;
		case Enumeradores.WIEGAND_FC_SEM_SEPARADOR:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.WIEGAND_FC_SEM_SEPARADOR);
			break;
		case Enumeradores.WIEGAND_FC_COM_SEPARADOR:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.WIEGAND);
			break;
		case Enumeradores.QRCODE:
			EasyInner.ConfigurarTipoLeitor(Enumeradores.BARRAS_PROX_QRCODE);
			break;
		}

	}

	// ESTADO_RECEBER_VERSAO_BIO
	private void PASS_STATE_RECEBER_VERSAO_BIO(Inner inner) throws InterruptedException {
		System.out.println("Inner " + inner.Numero + " Estado receber vers�o bio...");
		// SE FOR BIOMETRIA
		if (inner.VersaoFW.getFirwareSup() < 6) {
			inner.VersaoBio = BioService.ReceberVersaoBioAVer5xx(inner.Numero);
		} else {
			inner.VersaoBio = BioService.ReceberVersaoBio6xx(inner.Numero, inner.TipoComBio);
		}
		if (inner.VersaoBio != null && inner.VersaoBio != "Modulo incorreto") {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_CFG_OFFLINE;
		} else if ("Modulo incorreto".equals(inner.VersaoBio)) {
			stopMaquinaOnline();
			JOptionPane.showMessageDialog(null, "Modulo incorreto", "Mensagem", JOptionPane.INFORMATION_MESSAGE);
		} else {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
		}

	}

	// ESTADO_RECEBER_MODELO_BIO
	private void PASS_STATE_RECEBER_MODELO_BIO(Inner inner) throws InterruptedException {
		System.out.println("Inner " + inner.Numero + " Estado receber modelo bio...");
		if (inner.VersaoFW.getFirwareSup() < 6) {
			inner.ModeloBioInner = BioService.ReceberModeloBioAVer5xx(inner.Numero);
		} else {
			inner.ModeloBioInner = BioService.ReceberModeloBio6xx(inner.Numero, inner.TipoComBio);
		}
		if (inner.ModeloBioInner != null && inner.ModeloBioInner != "Modulo incorreto") {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_VERSAO_BIO;
		} else if ("Modulo incorreto".equals(inner.ModeloBioInner)) {
			stopMaquinaOnline();
			System.out.println("Modulo incorreto");
		} else {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
		}
	}

	private void stopMaquinaOnline() {
		stop = true;
		System.out.println("Maquina parada");
		ReturnStateInners(Enumeradores.EstadosInner.ESTADO_CONECTAR, Enumeradores.EstadosTeclado.AGUARDANDO_TECLADO);

	}

	private void ReturnStateInners(Enumeradores.EstadosInner statesInner, Enumeradores.EstadosTeclado keyboardStatus) {
		for (Object objInner : listInners.values()) {
			Inner inner = (Inner) objInner;
			inner.EstadoAtual = statesInner;
			inner.EstadoTeclado = keyboardStatus;
		}
	}

	// ESTADO_RECEBER_FIRWARE
	private void STATE_RECEIVE_FIRWARE(Inner inner) {
		Integer ret = 0;
		System.out.println("Inner " + inner.Numero + " Enviar receber vers�o FirWare...");
		int version[] = new int[16];
		// SOLICITA A VERS�O DO FIRMWARE DO INNER E DADOS COMO O IDIOMA.
		System.out.println("Inner " + inner.Numero + " Estado receber firware...");
		ret = EasyInner.ReceberVersaoFirmware6xx(inner.Numero, version);

		inner.VersaoFW.setLinhaProduto(version[0]);
		inner.VersaoFW.setVariacaoSup(version[1]);
		inner.VersaoFW.setVariacaoInf(version[2]);
		inner.VersaoFW.setFirwareSup(version[3]);
		inner.VersaoFW.setFirwareInf(version[4]);
		inner.VersaoFW.setFirwareSuf(version[5]);
		inner.VersaoFW.setTipoEquip(version[6]);
		inner.VersaoFW.setTipoModuloBio(version[7]);
		// SE SELECIONADO BIOMETRIA, VALIDA SE O EQUIPAMENTO � COMPARTIVEL
		if (inner.Biometrico) {
			if (inner.VersaoFW.getLinhaProduto() != 6 && inner.VersaoFW.getTipoEquip() == 0) {
				System.out.println("Equipamento " + String.valueOf(inner.Numero) + " n�o � compartivel com Biometria.");
			}
		}
		if (ret == Enumeradores.RET_COMANDO_OK) {
			// DEFINE A LINHA DO INNER
			switch (inner.VersaoFW.getLinhaProduto()) {
			case 1:
				inner.LinhaInner = "Inner Plus";
				break;
			case 2:
				inner.LinhaInner = "Inner Disk";
				break;
			case 3:
				inner.LinhaInner = "Inner Verid";
				break;
			case 6:
				inner.LinhaInner = "Inner Bio";
				break;
			case 7:
				inner.LinhaInner = "Inner NET";
				break;
			case 14:
				inner.LinhaInner = "Inner Acesso";
				inner.InnerNetAcesso = true;
				break;
			case 18:
				inner.LinhaInner = "Inner Acesso 2";
				inner.InnerNetAcesso = true;
				break;
			case 19:
				inner.LinhaInner = "Catraca 4";
				inner.InnerNetAcesso = true;
				break;
			}
		}
		inner.VersaoInner = Integer.toString(inner.VersaoFW.getFirwareSup()) + '.'
				+ Integer.toString(inner.VersaoFW.getFirwareInf()) + '.'
				+ Integer.toString(inner.VersaoFW.getFirwareSuf());
		if (inner.VersaoFW.getLinhaProduto() == 6 || inner.VersaoFW.getTipoEquip() == 1) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_MODELO_BIO;
		} else {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_CFG_OFFLINE;
		}

	}

	// PASSO_ESTADO_RECONECTAR
	private void STEP_STATE_RECONNECT(Inner inner) {
		try {
			Integer ret = 0;
			Long tmpTime = System.currentTimeMillis() - inner.TempoInicialPingOnLine;
			if (tmpTime < 10000) {
				return;
			}
			inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
			System.out.println("Inner " + inner.Numero + " Reconectando...");
			ret = Enumeradores.Limpar;
			initConnect = System.currentTimeMillis();
			ret = testInnerConnection(inner.Numero);
			// TESTA O COMANDO DE ENVIO DE RELOGIO PARA O INNER
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// ZERA AS VARIAVEIS DE CONTROLE DA MAQUINA DE ESTADOS.
				inner.CountTentativasEnvioComando = 0;
				// RECEBENDO VERSAO DE FIRMWARE, PASSO OBRIGATORIO
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_FIRWARE;
			} else {
				// CASO ELE N�O CONSIGA, TENTAR� ENVIAR 3 VEZES, SE N�O CONSEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
			inner.CountRepeatPingOnline = 0;

		} catch (Exception e) {
			System.out.println("Passo Reconectar :  " + e);
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_CONECTAR
	private void STEP_STATE_CONNECT(Inner inner) {
		Integer ret = 0;
		Long initConnect = 0L;
		System.out.println(inner.CountTentativasEnvioComando);
		try {
			ret = Enumeradores.Limpar;
			inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
			System.out.println("Inner " + inner.Numero + " Conectando...");
			initConnect = System.currentTimeMillis();
			ret = testInnerConnection(inner.Numero);
			System.out.println(ret);
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// CASO CONSIGA O INNER VAI PARA O PASSO DE CONFIGURA��O OFFLINE, POSTERIORMENTE
				// PARA COLETA DE BILHETES.
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_FIRWARE;
			} else {
				// CASO ELE N�O CONSIGA, TENTAR� ENVIAR 3 VEZES, SE N�O CONSEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
		}
		System.out.println(initConnect);

	}

	private Integer testInnerConnection(int numero) {
		int[] DataHora = new int[6];
		Integer ret = EasyInner.ReceberRelogio(numero, DataHora);
		return ret;
	}
}
