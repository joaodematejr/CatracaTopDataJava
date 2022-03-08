package com.alquimidia;

import java.util.HashMap;

import javax.swing.JOptionPane;

import com.alquimidia.easyInner.entity.Inner;
import com.alquimidia.enumeradores.Enumeradores;
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
			// CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
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
			// CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
			addInner(inner);
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunicação.");
		}
	}

	// INICIAR COMUNICAÇÃO COM A CATRACA
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
				default:
					break;
				}
			}
			Thread.sleep(1);
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
			System.out.println("inner.Acionamento" + inner.Acionamento);
			switch (inner.Acionamento) {
			case Enumeradores.Acionamento_Catraca_Urna:
				EasyInner.ConfigurarAcionamento1(Enumeradores.ACIONA_REGISTRO_ENTRADA_OU_SAIDA, 5);
				EasyInner.ConfigurarAcionamento2(Enumeradores.ACIONA_REGISTRO_SAIDA, 5);
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
				if ((inner.Acionamento == Enumeradores.Acionamento_Catraca_Entrada) || (inner.Acionamento == Enumeradores.Acionamento_Catraca_Saida_Liberada)) {
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
			EasyInner.DefinirNumeroCartaoMaster("99999999999999");
			System.out.println("parei aki");
		} catch (Exception e) {
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
