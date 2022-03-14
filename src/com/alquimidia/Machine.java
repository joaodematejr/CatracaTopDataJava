package com.alquimidia;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import com.alquimidia.entity.TicketGate;
import com.alquimidia.entity.UsuarioSemDigital;
import com.alquimidia.entity.Usuarios;
import com.alquimidia.enumeradores.Enumeradores;
import com.alquimidia.utils.EasyInnerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.topdata.BioService;
import com.topdata.EasyInner;

public class Machine {
	TicketGate ticketGate = new TicketGate();
	Gson gson = new Gson();
	Rlog log = new Rlog();
	private HashMap<Integer, Inner> listInners;
	private boolean stop;
	Long initConnect;

	// LIBERA ENTRADA
	boolean releaseEntry = false;
	// LIBERA ENTRADA INVERTIDA
	boolean releaseInvertedEntry = false;

	// FRAMETICKET
	String tokenApp = "de775dc0998cc5efe65903252085dccd";

	public Machine() {
		listInners = new HashMap<Integer, Inner>();
		stop = false;
	}

	public void addInner(Inner inner) {
		listInners.put(inner.Numero, inner);
	}

	public void startMachine() throws InterruptedException, IOException {
		Integer ret = 0;
		EasyInner.FecharPortaComunicacao();
		EasyInner.DefinirTipoConexao(2);
		ret = EasyInner.AbrirPortaComunicacao(3570);
		if (ret == EasyInner.RET_COMANDO_OK) {
			System.out.println("Porta Aberta");
			// CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
			
			// CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
			Reader reader = Files.newBufferedReader(Paths.get("C:\\Alquimidia\\config.json"));
			List<Inner> listInnerFiles = new Gson().fromJson(reader, new TypeToken<List<Inner>>() {}.getType());
	
			for (Inner tmpInner : listInnerFiles) {
				tmpInner.EstadoTeclado = Enumeradores.EstadosTeclado.TECLADO_EM_BRANCO;
				tmpInner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
				addInner(tmpInner);
			}
			
			reader.close();
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunicação.");
		}
	}

	// INICIAR COMUNICAÇÃO COM A CATRACA
	private void machine() throws InterruptedException, IOException {
		while (!stop) {
			for (Object objInner : listInners.values()) {
				Inner inner = (Inner) objInner;
				System.out.println("57 " + inner.EstadoAtual);
				log.doLogging("" + inner.EstadoAtual);
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
				// ESTADO_COLETAR_BILHETES
				case ESTADO_COLETAR_BILHETES:
					STEP_STATUS_COLLECT_TICKETS(inner);
					break;
				// ESTADO_PING_ONLINE
				case ESTADO_PING_ONLINE:
					STEP_STATE_SEND_PING_ONLINE(inner);
					break;
				// ESTADO_VALIDAR_ACESSO
				case ESTADO_VALIDAR_ACESSO:
					STEP_STATUS_VALIDATE_ACCESS(inner);
					break;
				// ESTADO_ENVIAR_MSG_ACESSO_NEGADO
				case ESTADO_ENVIAR_MSG_ACESSO_NEGADO:
					STEP_STATUS_SEND_MSG_ACCESS_DENIED(inner);
					break;
				// ESTADO_AGUARDA_TEMPO_MENSAGEM
				case ESTADO_AGUARDA_TEMPO_MENSAGEM:
					STEP_STATUS_WAITING_TIME_MESSAGE(inner);
					break;
				// ESTADO_LIBERAR_CATRACA
				case ESTADO_LIBERAR_CATRACA:
					STEP_STATUS_RELEASES_RATCHET(inner);
					break;
				// ESTADO_MONITORA_GIRO_CATRACA
				case ESTADO_MONITORA_GIRO_CATRACA:
					STEP_STATUS_MONITOR_RATCHET_TURN(inner);
					break;
				default:
					break;
				}
			}
			Thread.sleep(1);
		}
	}

	// PASSO_ESTADO_MONITORA_GIRO_CATRACA
	private void STEP_STATUS_MONITOR_RATCHET_TURN(Inner inner) {
		try {
			Integer ret = 0;
			int[] ticket = new int[8];
			StringBuffer card;
			card = new StringBuffer();
			// EXIBE ESTADO DO ESTADO_MONITORA_GIRO_CATRACA
			System.out.println("Monitorando Giro de Catraca!");
			// EXIBE ESTADO DO INNER NO RODAPÉ DA JANELA
			System.out.println("Inner " + inner.Numero + " Monitora Giro da Catraca...");
			// MONITOR O GIRO DA CATRACA
			ret = EasyInner.ReceberDadosOnLine(inner.Numero, ticket, card);
			// TESTA O RETORNO DO COMANDO
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// TESTA SE GIROU OU NÁO A CATRACA
				if (ticket[0] == Enumeradores.FIM_TEMPO_ACIONAMENTO) {
					System.out.println("Não girou a catraca");
				} else if (ticket[0] == Enumeradores.GIRO_DA_CATRACA_TOPDATA) {
					if (inner.CatInvertida) {
						if (Integer.parseInt(String.valueOf(ticket[1])) == 0) {
							System.out.println("Girou a catraca para saida.");
						} else {
							System.out.println("Girou a catraca para entrada.");
							ticketVoucher(inner);
						}
					} else {
						if (Integer.parseInt(String.valueOf(ticket[1])) == 0) {
							System.out.println("Girou a catraca para entrada.");
							ticketVoucher(inner);
						} else {
							System.out.println("Girou a catraca para saida.");
						}
					}
				}
				// VAI PARA O ESTADO DE ENVIO DE MENSAGEM PADRAO
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_PADRAO;
			} else {
				// CASO O TEMPO QUE ESTIVER MONITORANDO O GIRO CHEGUE A 3 SEGUNDOS
				// DEVERA ENVIAR O PING ON LINE PARA MANTER O EQUIPAMENTO EM MODO ON LINE
				long tempo = (System.currentTimeMillis() - inner.TempoInicialPingOnLine);
				if (tempo >= 3000) {
					inner.EstadoSolicitacaoPingOnLine = inner.EstadoAtual;
					inner.CountTentativasEnvioComando = 0;
					inner.TempoInicialPingOnLine = System.currentTimeMillis();
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_PING_ONLINE;
				}
			}

		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	private void ticketVoucher(Inner inner) throws IOException {
		ticketGate.setId_catraca(String.valueOf(inner.Numero));

		URL url = new URL(inner.EndPoint + "/catraca/valida-ticket/" + inner.BilheteInner.Cartao);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("ft-tokenapp", tokenApp);
		http.setRequestProperty("ft-appclient", inner.AppClient);

		String req = gson.toJson(ticketGate);

		try (OutputStream os = http.getOutputStream()) {
			byte[] input = req.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

			TicketGate ticketGateClass = gson.fromJson(response.toString(), TicketGate.class);

			String res = ticketGateClass.getStatus();

			switch (res) {
			case "OK":
				EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0, "                INGRESSO OK");
				break;
			case "FAIL":
				EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0, "                INGRESSO UTILIZADO");
				break;
			default:
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
				break;
			}
		}

		http.disconnect();

	}

	// PASSO_ESTADO_LIBERA_GIRO_CATRACA
	private void STEP_STATUS_RELEASES_RATCHET(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER
			System.out.println("Inner " + inner.Numero + " Libera Giro da Catraca...");
			if (releaseEntry) {
				EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0, "                ENTRADA LIBERADA");
				releaseEntry = false;
				ret = EasyInner.LiberarCatracaEntrada(inner.Numero);
			} else if (releaseInvertedEntry) {
				EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0, "                ENTRADA LIBERADA");
				releaseInvertedEntry = false;
				ret = EasyInner.LiberarCatracaEntradaInvertida(inner.Numero);
			} else {
				EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0, "LIBERADO DOIS SENTIDOS");
				ret = EasyInner.LiberarCatracaDoisSentidos(inner.Numero);
			}
			// TESTA RETORNO DO COMANDO
			if (ret == Enumeradores.RET_COMANDO_OK) {
				EasyInner.AcionarBipCurto(inner.Numero);
				inner.CountPingFail = 0;
				inner.CountTentativasEnvioComando = 0;
				inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_MONITORA_GIRO_CATRACA;
			} else {
				// SE O RETORNO FOR DIFERENTE DE 0 TENTA LIBERAR A CATRACA 3 VEZES, CASO NÃO
				// CONSIGA ENVIAR O COMANDO VOLTA PARA O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.CountTentativasEnvioComando = 0;
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_AGUARDA_TEMPO_MENSAGEM
	private void STEP_STATUS_WAITING_TIME_MESSAGE(Inner inner) {
		try {
			System.out.println("Inner " + inner.Numero + " Aguardar tempo mensagem...");
			// APOS PASSAR OS 2 SEGUNDOS VOLTA PAR AO PASSO ENVIAR MENSAGEM PADRAO
			long tempo = (int) System.currentTimeMillis() - inner.TempoInicialMensagem;
			if (tempo > 2000) {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_PADRAO;
				EasyInner.DesligarLedVermelho(inner.Numero);
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_MSG_ACESSO_NEGADO
	private void STEP_STATUS_SEND_MSG_ACCESS_DENIED(Inner inner) {
		try {
			System.out.println("Inner " + inner.Numero + " Enviar mensagem acesso negado...");
			// TESTA O RETORNO DO COMANDO DE ENVIO DE MENSAGEM PADRAO ONLINE
			if (EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 0,
					" Acesso Negado!  NAO AUTORIZADO \r\n") == Enumeradores.RET_COMANDO_OK) {
				inner.TempoInicialMensagem = System.currentTimeMillis();
				EasyInner.AcionarBipLongo(inner.Numero);
				// if (inner.InnerNetAcesso) {
				EasyInner.LigarLedVermelho(inner.Numero);
				// }
				// MUDA O PASSO PARA CONFIGURAÇÃO DE ENTRADA ONLINE
				inner.TempoInicialMensagem = (int) System.currentTimeMillis();
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_AGUARDA_TEMPO_MENSAGEM;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARA ENVIAR TRES VEZES SE NÃO CONSEGUIR VOLTA PAR AO
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				// ADICIONA 1 AO CONTADOR DE TENTATIVAS
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_VALIDAR_ACESSO
	private void STEP_STATUS_VALIDATE_ACCESS(Inner inner) throws IOException {
		// INNER ESTADO ATUAL = ENUMERADORES ESTADOS INNER ESTADO_DEFINE_PROCESSO;
		// E URNA OU ENTRADA E SAIDA OU LIBERADO 2 SENTIDOS OU SENTIDOS GIRO
		// E CARTAO = PROXIMIDADE
		System.out.println("Inner " + inner.Numero + " Validar Acesso...");

		// String codTicket = "260551405";

		ticketGate.setId_catraca(String.valueOf(inner.Numero));

		URL url = new URL(inner.EndPoint + "/catraca/consulta-ticket/" + inner.BilheteInner.Cartao);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("ft-tokenapp", tokenApp);
		http.setRequestProperty("ft-appclient", inner.AppClient);

		String req = gson.toJson(ticketGate);

		try (OutputStream os = http.getOutputStream()) {
			byte[] input = req.getBytes("utf-8");
			os.write(input, 0, input.length);
		}

		try (BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}

			TicketGate ticketGateClass = gson.fromJson(response.toString(), TicketGate.class);

			String res = ticketGateClass.getStatus();

			switch (res) {
			case "OK":
				enableSideTurnstile("Entrada", inner.CatInvertida);
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_LIBERAR_CATRACA;
				break;
			case "FAIL":
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_ACESSO_NEGADO;
				break;
			default:
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
				break;
			}
		}

		http.disconnect();

	}

	// HabilitarLadoCatraca
	private void enableSideTurnstile(String side, boolean catInvertida) {
		if (side.equals("Entrada")) {
			// ENTRADA
			if (catInvertida == false) {
				releaseEntry = true;
				releaseInvertedEntry = false;
			} else {
				releaseInvertedEntry = true;
				releaseEntry = false;
			}
		}

		if (side.equals("Saida")) {
			// SAIDA
			if (catInvertida == false) {
				releaseEntry = true;
				releaseInvertedEntry = false;
			} else {
				releaseInvertedEntry = true;
				releaseEntry = false;
			}
		}

	}

	// PASSO_ESTADO_ENVIA_PING_ONLINE
	private void STEP_STATE_SEND_PING_ONLINE(Inner inner) {
		try {
			// EXIBE ESTADO DO INNER NO RODAPÉ DA JANELA
			System.out.println("Inner " + inner.Numero + " PING ONLINE...");
			// ENVIAR O COMANDO DE PING ON ONLINE, SE O RETORNO FOR OK VOLTA PARA O ESTADO
			// ONDE CHAMOU O ESTADO
			Integer retorno = EasyInner.PingOnLine(inner.Numero);
			if (retorno == EasyInner.RET_COMANDO_OK) {
				inner.EstadoAtual = inner.EstadoSolicitacaoPingOnLine;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARA ENVIAR TRES VEZES, SE NAO CONSEGUIR VOLTAR PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
			inner.TempoInicialPingOnLine = System.currentTimeMillis();
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			System.out.println(e);
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
		}

	}

	// PASSO_ESTADO_COLETAR_BILHETES
	private void STEP_STATUS_COLLECT_TICKETS(Inner inner) throws InterruptedException {
		if (inner.InnerNetAcesso) {
			// ColetarBilhetesInnerAcesso(inner);
			collectTicketsInnerNet(inner);
		} else {
			// ColetarBilhetesInnerAcesso(inner);
			collectTicketsInnerNet(inner);
			if (inner.Catraca) {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_OFFLINE_CATRACA;
			} else {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_MSG_OFFLINE_COLETOR;
			}
		}

	}

	// ColetarBilhetesInnerAcesso
	private void collectTicketsInnerNet(Inner inner) throws InterruptedException {
		int[] ticket = new int[8];
		StringBuffer card;
		Integer nTicket;
		int ret = 0;
		System.out.println("Inner " + inner.Numero + " Coletar bilhete Inner acesso...");
		// VERIFICA CONEXAO
		nTicket = 0;
		if (inner.BilhetesAReceber > 0) {
			card = new StringBuffer();
			// COLETA UM BILHETE OFF-LINE QUE ESTÁ ARMAZENADO NA MEMORIA DO INNER
			ret = EasyInner.ColetarBilhete(inner.Numero, ticket, card);
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// ARMAZENA OS DADOS DO BILHETE NO LIST, PODE SER UTILIZADO COM BANCO DE DADOS
				// OU OUTRO MEIO DE ARMAZENAMENTO
				System.out
						.println(
								"Tipo:" + String.valueOf(ticket[0]) + " CartÃ£o:" + card.toString() + " Data:"
										+ (String.valueOf(ticket[1]).length() == 1 ? "0" + String.valueOf(ticket[1])
												: String.valueOf(ticket[1]))
										+ "/"
										+ (String.valueOf(ticket[2]).length() == 1 ? "0" + String.valueOf(ticket[2])
												: String.valueOf(ticket[2]))
										+ "/" + String.valueOf(ticket[3]) + " Hora:"
										+ (String.valueOf(ticket[4]).length() == 1 ? "0" + String.valueOf(ticket[4])
												: String.valueOf(ticket[4]))
										+ ":"
										+ (String.valueOf(ticket[5]).length() == 1 ? "0" + String.valueOf(ticket[5])
												: String.valueOf(ticket[5]))
										+ ":"
										+ (String.valueOf(ticket[6]).length() == 1 ? "0" + String.valueOf(ticket[6])
												: String.valueOf(ticket[6]))
										+ "\n");
				nTicket++;
				inner.BilhetesAReceber--;
			}
			System.out.println("Foram coletados " + nTicket + " bilhete(s) offline !");
		}
		if (inner.BilhetesAReceber == 0) {
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_QTD_BILHETES_OFF;
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
				// ATRIBUINDO OS DADOS DO CARTÃO AO INNER ATUAL
				inner.BilheteInner.Origem = iArrBcardRb[0];
				inner.BilheteInner.Complemento = iArrBcardRb[1];
				inner.BilheteInner.Dia = iArrBcardRb[2];
				inner.BilheteInner.Mes = iArrBcardRb[3];
				inner.BilheteInner.Ano = iArrBcardRb[4];
				inner.BilheteInner.Hora = iArrBcardRb[5];
				inner.BilheteInner.Minuto = iArrBcardRb[6];
				inner.BilheteInner.Segundo = iArrBcardRb[7];

				// LIMPANDO A VARIAVEL STRINGBUILDER E ATRIBUINDO UM NOVO VALOR
				inner.BilheteInner.Cartao.setLength(0);
				inner.BilheteInner.Cartao = new StringBuilder(card.toString());

				// MontarBilheteRecebido(inner);
				assembleTicketReceived(inner);
				// PreencherBilheteDisplay(inner);
				fillTicketDisplay(inner);
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_VALIDAR_ACESSO;

			} else {
				long temp = System.currentTimeMillis() - inner.TempoInicialPingOnLine;
				// SE PASSAR 3 SEGUNDOS SEM RECEBER NADA, PASSA PARA O ESTADO ENVIAR PING
				// ONLINE, MANTER O EQUIPAMENTO EM ON LINE.
				if ((int) temp > 3000) {
					inner.EstadoSolicitacaoPingOnLine = inner.EstadoAtual;
					inner.CountTentativasEnvioComando = 0;
					inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_PING_ONLINE;
				}
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			System.err.println(e);
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PreencherBilheteDisplay
	private void fillTicketDisplay(Inner inner) throws IOException {
		// SE QUANTIDADE DE DIGITO INFORMADO FOR MAIOR QUE 14 NÃO DEVE MOSTRAR DATA E
		// HORARIO
		if (inner.QtdDigitos <= 14) {
		} else {
		}

	}

	// MontarBilheteRecebido
	private void assembleTicketReceived(Inner inner) {
		String sTicketReceived = inner.BilheteInner.Cartao.toString();
		Integer tam;
		if (inner.QtdDigitos > sTicketReceived.length()) {
			tam = sTicketReceived.length();
		} else {
			tam = inner.QtdDigitos;
		}
		String sNumCartao = "";
		// SE O CARTAO PADRAO FOR TOPDATA, CONFIGURAR OS DIGITOS DO CARTAO COMO PADRAO
		// TOPDATA
		if (inner.PadraoCartao == 0) {
			// PADRAO TOPDATA --> CARTAO TOP DATA DEVE SER SEMPRE 14 DIGITOS
			sNumCartao = Long.toString(Long.parseLong(sTicketReceived.substring(0, tam)));
			sNumCartao = sNumCartao.substring(13, 14) + "" + sNumCartao.substring(4, 8);
			inner.BilheteInner.Cartao.delete(0, inner.BilheteInner.Cartao.length());
			inner.BilheteInner.Cartao = new StringBuilder(sNumCartao);
		} else {
			sNumCartao = EasyInnerUtils.remZeroEsquerda(inner.BilheteInner.Cartao.toString());
			// GRAVANDO NO INNER O NUMERO DO CARTAO FORMATADO
			inner.BilheteInner.Cartao.delete(0, inner.BilheteInner.Cartao.length());
			inner.BilheteInner.Cartao = new StringBuilder(sNumCartao);
		}
	}

	// PASSO_ESTADO_CONFIGURAR_ENTRADAS_ONLINE
	private void STAGE_STEP_CONFIGURE_ONLINE_ENTRIES(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER DO RODAPÉ DA JANELA
			System.out.println("Inner " + inner.Numero + " Configurando Entradas Online...");
			// DECLARAÇÃO DE VARIAVEIS
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
				// CASO ELE NÃO CONSIGA, TENTARA ENVIAR TRES VEZES SE NÃO CONSEGUIR VOLTA PARA O
				// PASSO ESTADO_RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				// ADICIONA 1 AO CONTADOR DE TENTANTIVAS
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_MSG_PADRAO
	private void STEP_STATUS_SEND_STANDARD_MSG(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER DO RODAPÉ DA JANELA
			System.out.println("Inner " + inner.Numero + " Enviando Mensagem Padrão...");
			// DECLARAÇÃO DE VARIAVEIS
			ret = Enumeradores.Limpar;
			// ENVIAR COMANDO DEFININDO A MENSAGEM PADRÃO ONLINE PARA O INNER
			ret = EasyInner.EnviarMensagemPadraoOnLine(inner.Numero, 1, " Frameticket 5.0");
			// TESTA O RETORNO DA MENSAGEM ENVIADA
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// MUDA O PASSO PARA CONFIGURAÇÃO DE ENTRADA ONLINE
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONFIGURAR_ENTRADAS_ONLINE;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARA ENVIAR TRES VEZES, SE NÃO CONEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				// ADICIONAR 1 AO CONTADOR DE TENTANTIVAS
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE
	private void STEP_STATUS_SEND_CONFIGMUD_ONLINE_OFFLINE(Inner inner) {
		try {
			Integer ret = 0;
			System.out.println("Inner " + inner.Numero + " Enviar mudancas on off...");
			// HABILITA/DESABILITA A MUDANÇA AUTOMATICA DO MODO OFFILINE DO INNER PARA
			// ONLINE E VICE-VERSA
			EasyInner.HabilitarMudancaOnLineOffLine(2, 10);
			// CONFIGURA O TECLADO PARA QUANDO O INNER VOLTAR PARA ONLINE APENAS UMA QUEDA
			// PARA OFFLINE
			EasyInner.DefinirConfiguracaoTecladoOnLine(inner.QtdDigitos, 1, 5, 17);
			// DEFINE MUDANÇAS ONLINE
			// FUNÇÃO QUE CONFIGURA BIT A BIT, VER NO MANUAL ANEXO III
			EasyInner.DefinirEntradasMudancaOnLine(configureEntriesChangeOnLine(inner));
			if (inner.Biometrico) {
				// CONFIGRA ENTRADAS MUDANÇAS OFFLINE COM BIOMETRIA
				EasyInner.DefinirEntradasMudancaOffLineComBiometria(
						(inner.Teclado ? Enumeradores.Opcao_SIM : Enumeradores.Opcao_NAO), 3,
						(byte) (inner.DoisLeitores ? 3 : 0), inner.Verificacao, inner.Identificacao);
			} else {
				// CONFIGURA ENTRADAS MUDANÇA OFFLINE
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

			// DEFINE MENSAGEM DE ALTERAÇÃO ONLINE -> OFFLINE
			EasyInner.DefinirMensagemPadraoMudancaOffLine(1, " Modo OffLine");
			// DEFINE MENSAGEM DE ALTERAÇÃO OFFILINE -> ONLINE
			EasyInner.DefinirMensagemPadraoMudancaOnLine(1, " Frameticket 5.0");
			// ENVIA CONFIGURAÇÕES
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
			log.doLogging(e.getMessage());
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
			 * -----------------------------------------------------------------------------
			 * --------------------- | 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |
			 * -----------------------------------------------------------------------------
			 * --------------------- | SETA/RESETA | Bit 2 | Bit 1 | Bit 0 | Bit 2 | Bit 1 |
			 * Bit 0 | TECLADO | | CONFIG. | LEITOR 2 | | | | | | | | bit-a-bit | | | | | |
			 * | |
			 * -----------------------------------------------------------------------------
			 * --------------------- | 1 HABILITA | 000 - DESATIVAR LEITOR | 000 - DESATIVAR
			 * LEITOR | 1 HABILITA | | 0 DESABILITA | 001 - LEITOR ENTRADA | 001 - LEITOR
			 * ENTRADA | 0 DESABILITA | | | 010 - LEITOR SAIDA | 010 - LEITOR SAIDA | | | |
			 * 011 - LEITOR ENTRADA E SAIDA | 011 - LEITOR ENTRADA E SAIDA | | | | 100 -
			 * LEITOR ENTRADA E SAIDA | 100 - LEITOR ENTRADA E SAIDA | | | | INVERTIDO |
			 * INVERTIDO | |
			 * -----------------------------------------------------------------------------
			 * ---------------------
			 */
		} else {
			// BIT FIXO + HABILITADO + INDENTIFICAÇÃO + VERIFICACAO + BIT FIXO + HABILITAR
			// LEITOR + HABILITAR APENAS LEITOR
			settings = "0" + "1" + inner.Identificacao + inner.Verificacao + "0" + (inner.DoisLeitores ? "11" : "10")
					+ settings;

			/*
			 * -----------------------------------------------------------------------------
			 * ------------------------------------------- | 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |
			 * -----------------------------------------------------------------------------
			 * ------------------------------------------- | Bit FIXO | SETA/RESETA |
			 * IDENTIFICAÇÃO | VERIFICAÇÃO | Bit FIXO | LEITOR 1 | LEITOR 2 | TECLADO | |
			 * '0' | CONFIG. | BIO | BIO | CONFIG | | | | | | bit-a-bit BIO | | | L2 | | | |
			 * | | | | | '0' | | | |
			 * -----------------------------------------------------------------------------
			 * ------------------------------------------- | 0 | 1-HABILITA | 1-HABILITA |
			 * 1-HABILITA | 1-HABILITA | 1-HABILITA | 1-HABILITA | 1-HABILITA | | |
			 * 0-DESABILITA | 0-DESABILITA | 0-DESABILITA | 0-DESABILITA | 0-DESABILITA |
			 * 0-DESABILITA | 0-DESABILITA |
			 * -----------------------------------------------------------------------------
			 * -------------------------------------------
			 */
		}
		// CONVERTER BINARIO PARA DECIMAL
		return EasyInnerUtils.BinarioParaDecimal(settings);
	}

	// PASSO_ESTADO_ENVIAR_CFG_ONLINE
	private void STEP_STATE_ENVIAR_CFG_ONLINE(Inner inner) {
		try {
			System.out.println("Inner " + inner.Numero + " Enviar configuração Online...");
			Integer ret = 0;
			// MONTAR CONFIGURAÇÃO MODO ONLINE
			mountConfiguracaoInner(inner, Enumeradores.MODO_ON_LINE);
			// ENVIAR AS CONFIGURAÇÕES AO INNER ATUAL
			ret = EasyInner.EnviarConfiguracoes(inner.Numero);
			if (ret == Enumeradores.RET_COMANDO_OK) {
				// CASO CONSIGA ENVIAR AS CONFIGURAÇÕES, PASSA PARA O PASSO ENVIAR DATA HORA
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_CONFIGMUD_ONLINE_OFFLINE;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARA ENVIAR TRÊS VEZES, SE NÃO CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_CONECTAR;
		}

	}

	// PASSO_ESTADO_ENVIAR_DATA_HORA
	private void STEP_STATE_SEND_DATE_TIME(Inner inner) {
		try {
			Integer ret = 0;
			// EXIBE ESTADO DO INNER NO RODAPÉ DA JANELA
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
				// CASO ELE NÃO CONSIGA, TENTARA, ENVIAR TRES VEZES, SE NÃO CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
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
			log.doLogging(e.getMessage());
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
				// CHAMA ROTINA QUE MONTA O BUFFER DE CARTOES QUE NÃO IRAO PRECISAR DA DIGITAL
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
			log.doLogging(e.getMessage());
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
			log.doLogging(ex.getMessage());
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
				// DEFINE A LISTA DE VERIFICAÇÃO
				if (inner.PadraoCartao == 1) {
					// MontarListaPadraoLivre(inner.QtdDigitos);
					assembleStandardListFree(inner.QtdDigitos);
				} else {
					// MontarListaPadraoTopdata(inner.QtdDigitos);
					assembleStandardTopdataList(inner.QtdDigitos);
				}
				// DEFINE QUAL TIPO DE LISTA(CONTROLE) DE ACESSO O INNER VAI UTILIZAR
				// UTILIZAR LISTA BRANCA (CARTÕES FORA DA LISTA TEM O ACESSO NEGADO)
				EasyInner.DefinirTipoListaAcesso(1);
				ret = EasyInner.EnviarListaAcesso(inner.Numero);
			} else {
				// NÃO UTILIZAR A LISTA DE ACESSO
				EasyInner.DefinirTipoListaAcesso(0);
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_SEMDIGITAL;
			}
			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_SEMDIGITAL;
			} else {
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
			}
		} catch (IOException | ClassNotFoundException | SQLException ex) {
			log.doLogging(ex.getMessage());
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
			log.doLogging(e.getMessage());
			System.out.println(e);
		}

	}

	private void setSchedules() throws IOException, FileNotFoundException, ClassNotFoundException, SQLException {
		@SuppressWarnings("unchecked")
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
			System.out.println("Inner " + inner.Numero + " Enviado configurações OFF-LINE...");
			// PREENCHE OS CAMPOS DE CONFIGURAÇÕES DO INNER
			// MontaConfiguracaoInner
			mountConfiguracaoInner(inner, Enumeradores.MODO_OFF_LINE);
			// ENVIA O COMANDO DE CONFIGURAÇÃO
			ret = EasyInner.EnviarConfiguracoes(inner.Numero);
			// TESTA O RETORNO DO ENVIO DAS CONFIGURAÇÕES OFF LINE
			if (ret == Enumeradores.RET_COMANDO_OK) {
				inner.CountTentativasEnvioComando = 0;
				// VERIFICA SE O ENVIAR LISTA ESTA SELECIONADO
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_ENVIAR_LISTA_OFFLINE;
				inner.TempoColeta = (int) System.currentTimeMillis() + 3000;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARA, ENVIAR TRÊS VEZES, SE NÃO CONSEGUIR VOLTA PARA
				// O PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
			System.out.println("120" + e.getMessage());
		}

	}

	// MONTAR CONFIGURAÇÃO INNER
	// MontaConfiguracaoInner
	private void mountConfiguracaoInner(Inner inner, int modoOffLine) {
		try {
			// ANTES DE REAILIZAR A CONFIGURAÇÃO PRECISA DEFINIR O PADRÃO DA CARTÃO
			if (inner.PadraoCartao == 1) {
				EasyInner.DefinirPadraoCartao(Enumeradores.PADRAO_LIVRE);
			} else {
				EasyInner.DefinirPadraoCartao(Enumeradores.PADRAO_TOPDATA);
			}
			// DEFINE MODO DE COMUNICAÇÃO
			if (modoOffLine == Enumeradores.MODO_OFF_LINE) {
				// CONFIGURAÇÃO PARA MODO OFFLINE
				// PREPARA O INNER PARA TRABALHAR NO MODO OFF LINE.
				// AINDA NÃO ENVIA ESSA INFORMAÇÃO PARA O EQUIPAMENTO.
				EasyInner.ConfigurarInnerOffLine();
			} else {
				// CONFIGURAÇÃO PARA MODO ONLINE
				// PREPARA O INNER PARA TRABALHAR NO MODO ON-LINE
				// AINDA NÃO ENVIA ESSA INFORMAÇÃO PARA O EQUIPAMENTO.
				EasyInner.ConfigurarInnerOnLine();
			}
			// VERIFICAR
			// ACIONAMENTOS 1 E 2
			// CONFIGURA COMO IRA O FUNCIONAR O ACIONAMENTO(RELE) 1 E 2 DO INNER, E POR
			// QUANTO TEMPO SERÁ ACIONADO
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
			// CARTÃO DO TIPO PROXIMIDADE NO LEITOR DO INNER, SEM QUE O USUARIO TENHA
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
			// CONFIGURA O TIPO DE REGISTRO QUE SERÁ ASSOCIADO A UMA MARCAÇÃO
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
			// CASO DESEJAR CONFIGURAR O INNER PARA LER CARTÕES QUE POSSAM VARIAR DE 1
			// DIGITO A 16 DIGITOS
			// UTILIZAR A FUNCAO InserirQuantidadeDigitoVariavel
			// EasyInner.InserirQuantidadeDigitoVariavel(8);
			// EasyInner.InserirQuantidadeDigitoVariavel(10);
			// EasyInner.InserirQuantidadeDigitoVariavel(14);
			if (inner.VersaoFW.getFirwareSup() >= 5) {
				EasyInner.ConfigurarBioVariavel(1);
			}

		} catch (Exception e) {
			log.doLogging(e.getMessage());
			System.out.println("133" + e.getMessage());
		}
	}

	// DEFINE VALORES PARA CONFIGURAR O LEITORES
	private void setsValuesConfigureReaders(Inner inner) {
		// CONFIGURAÇÃO CATRACA ESQUERDA OU DIREITA
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
		System.out.println("Inner " + inner.Numero + " Estado receber versão bio...");
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
		System.out.println("Inner " + inner.Numero + " Enviar receber versão FirWare...");
		int version[] = new int[16];
		// SOLICITA A VERSÃO DO FIRMWARE DO INNER E DADOS COMO O IDIOMA.
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
		// SE SELECIONADO BIOMETRIA, VALIDA SE O EQUIPAMENTO É COMPARTIVEL
		if (inner.Biometrico) {
			if (inner.VersaoFW.getLinhaProduto() != 6 && inner.VersaoFW.getTipoEquip() == 0) {
				System.out.println("Equipamento " + String.valueOf(inner.Numero) + " não é compartivel com Biometria.");
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
				// CASO ELE NÃO CONSIGA, TENTARÃ ENVIAR 3 VEZES, SE NÃO CONSEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
			inner.CountRepeatPingOnline = 0;

		} catch (Exception e) {
			log.doLogging(e.getMessage());
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
				// CASO CONSIGA O INNER VAI PARA O PASSO DE CONFIGURAÇÃO OFFLINE, POSTERIORMENTE
				// PARA COLETA DE BILHETES.
				inner.CountTentativasEnvioComando = 0;
				inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECEBER_FIRWARE;
			} else {
				// CASO ELE NÃO CONSIGA, TENTARÃ ENVIAR 3 VEZES, SE NÃO CONSEGUIR VOLTA PARA O
				// PASSO RECONECTAR
				if (inner.CountTentativasEnvioComando >= 3) {
					inner.EstadoAtual = Enumeradores.EstadosInner.ESTADO_RECONECTAR;
				}
				inner.CountTentativasEnvioComando++;
			}
		} catch (Exception e) {
			log.doLogging(e.getMessage());
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
