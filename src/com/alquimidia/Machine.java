package com.alquimidia;

import java.util.HashMap;

import com.alquimidia.easyInner.entity.Inner;
import com.alquimidia.enumeradores.Enumeradores;
import com.topdata.EasyInner;

public class Machine {
	private HashMap<Integer, Inner> listInners;
	private boolean stop;

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
			//CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
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
			//CONFIGURAÇÕES QUE PRECISAM CARREGAR DE UM ARQUIVO
			addInner(inner);
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunicação.");
		}
	}

	private void machine() throws InterruptedException {
		while (!stop) { 
			for(Object objInner : listInners.values()) {
				Inner inner = (Inner)objInner;
				switch (inner.EstadoAtual) {
				case ESTADO_CONECTAR:
					//PASSO_ESTADO_CONECTAR
					STEP_STATE_CONNECT(inner);
					break;

				default:
					break;
				}
			}
			Thread.sleep(1);
		}

	}

	private void STEP_STATE_CONNECT(Inner inner) {
		Integer ret = 0;
		Long initConnect = 0L;
		Long time = 0L;
		try {
			ret = Enumeradores.Limpar;
			inner.TempoInicialPingOnLine = (int) System.currentTimeMillis();
			System.out.println("Inner " + inner.Numero + " Conectando...");
			initConnect = System.currentTimeMillis();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
}
