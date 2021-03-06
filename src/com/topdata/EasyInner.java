package com.topdata;

public class EasyInner {

	static {
		System.loadLibrary("EasyInner");
	}

	public final static int RET_COMANDO_OK = 0;
	public final int RET_COMANDO_ERRO = 1;
	public final int RET_PORTA_NAOABERTA = 2;
	public final int RET_PORTA_JAABERTA = 3;
	public final int RET_DLL_INNER2K_NAO_ENCONTRADA = 4;
	public final int RET_DLL_INNERTCP_NAO_ENCONTRADA = 5;
	public final int RET_DLL_INNERTCP2_NAO_ENCONTRADA = 6;
	public final int RET_ERRO_GPF = 8;
	public final int RET_TIPO_CONEXAO_INVALIDA = 9;

	/**
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarListaUsuariosComDigital(int Inner);

	/**
	 *
	 * @param Inner
	 * @param Usuario
	 * @return
	 */
	public static native int SolicitarDigitalUsuario(int Inner, String Usuario);

	/**
	 *
	 * @param Inner
	 * @param Usuario
	 * @return
	 */
	public static native int ReceberDigitalUsuario(int Inner, String Usuario);

	/**
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Template
	 * @return
	 */
	public static native int ReceberDigitalUsuario(int Inner, byte[] Template, int TamResposta);

	/*
	 * ReceberRespostaRequisicaoBio
	 */
	public static native int ReceberRespostaRequisicaoBio(int Inner, int[] TamResposta);

	/**
	 *
	 * @param Inner
	 * @return
	 */
	public static native int ConfigurarComportamentoIndexSearch(int Inner);

	/**
	 * Habilita para utilizar usuarios com 16 digitos para biometria
	 *
	 * @param habilitado
	 * @return
	 */
	public static native int ConfigurarBioVariavel(int habilitado);

	/**
	 *
	 * @param Usuario
	 * @return
	 */
	public static native int ReceberUsuarioComDigital(byte[] Usuario);

	public static native int CMDTESTE(int i);

	/**
	 * Ler giros realizados na catraca
	 *
	 * @param Inner
	 * @param entradas
	 * @param saidas
	 * @param desistencias
	 * @return
	 */
	public static native int LerContadorGiro(int Inner, byte[] entradas, byte[] saidas, byte[] desistencias);

	/**
	 * Atribui valor ao contador de giros da catraca
	 *
	 * @param Inner
	 * @param entradas
	 * @param saidas
	 * @param desistencias
	 * @return
	 */
	public static native int AtribuirContadorGiro(int Inner, byte[] entradas, byte[] saidas, byte[] desistencias);

	/**
	 *
	 * @param Inner
	 * @param Setor
	 * @param cartao
	 * @param bloco_0
	 * @param bloco_1
	 * @param bloco_2
	 * @return
	 */
	public static native int LerSmartCard(int Inner, int Setor, byte[] cartao, byte[] bloco_0, byte[] bloco_1,
			byte[] bloco_2);

	/**
	 * Comando Novo Envia com template para o Inner Bio cadastrar no seu banco de
	 * dados. O resultado do cadastro deve ser verificado no retorno da fun????o
	 * UsuarioFoiEnviado. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para
	 * comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa. Template: O cadastro do usu??rio j?? contendo as duas digitais e o
	 * n??mero do usu??rio. ?? um array de bytes com o tamanho de 844 bytes.
	 *
	 * @param Inner
	 * @param usuario
	 * @param Template1
	 * @param Template2
	 * @return
	 */
	public static native int EnviarDigitalUsuario(int Inner, String usuario, byte[] Template1, byte[] Template2);

	/**
	 * Solicita uma digital diretaqmente do leitor do Inner
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarTemplateLeitor(int Inner);

	/**
	 * recebe a digital previamente solicitada
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Template
	 * @return
	 */
	public static native int ReceberTemplateLeitor(int Inner, int OnLine, byte[] Template);

	/**
	 * Configura qual ser?? o n??mero do cart??o master que o Inner ir?? aceitar. V??lido
	 * somente para o padr??o livre de cart??o. Para o padr??o Topdata o n??mero do
	 * master sempre ?? 0.
	 *
	 * Master 0 a 99999999999999 (M??ximo de 14 d??gitos) 0(Default).
	 *
	 * @param Master
	 * @return
	 */
	public static native int DefinirNumeroCartaoMaster(String Master);

	/**
	 *
	 * @param Sensor
	 * @param Evento
	 * @param Tempo
	 * @return
	 */
	public static native int DefinirEventoSensor(int Sensor, int Evento, int Tempo);

	/**
	 * Recebe o status atual dos sensores do Inner. Essa fun????o dever?? ser utilizada
	 * somente em casos muito espec??ficos, por exemplo, quando voc?? possui um Inner
	 * Plus/NET conectado a um sensor de presen??a e deseja saber se existe alguma
	 * pessoa naquele local Retorna o status do respectivo sensor: 0 ??? em n??vel
	 * baixo (fechado) 1 ??? em n??vel alto (aberto)
	 *
	 * @param Inner
	 * @param Sensores
	 * @return
	 */
	public static native int LerSensoresInner(int Inner, int[] Sensores);

	/**
	 * Para usar cartoes com diferentes quantidade de digitos
	 *
	 * @param Digito
	 * @return
	 */
	public static native int InserirQuantidadeDigitoVariavel(int Digito);

	/**
	 * Este comando funciona somente com o Inner no modo Off-Line. Recebe a
	 * quantidade de bilhetes que h?? no Inner para coletar.
	 *
	 * @param Inner
	 * @param QtdeBilhetes
	 * @return
	 */
	public static native int ReceberQuantidadeBilhetes(int Inner, int[] QtdeBilhetes);

	/**
	 * Este comando recebe do Inner o relogio com os parametros abaixo. Tab??m
	 * utiliza-se para verificar se o Inner esta conectado
	 *
	 *
	 * @param Inner
	 * @param dataHora
	 * @return
	 */
	public static native int ReceberRelogio(int Inner, int[] dataHora);

	/**
	 * aciona rele 1 para coletor
	 *
	 * @param Inner
	 * @return
	 */
	public static native int AcionarRele1(int Inner);

	/**
	 * aciona rele 2 para coletor
	 *
	 * @param Inner
	 * @return
	 */
	public static native int AcionarRele2(int Inner);

	/**
	 * Define qual ser?? o tipo de conex??o(meio de comunica????o) que ser?? utilizada
	 * pela dll para comunicar com os Inners. Essa fun????o dever?? ser chamada antes
	 * de iniciar o processo de comunica????o e antes da fun????o AbrirPortaComunicacao.
	 * 0 - Comunica????o serial, RS-232/485. 1 - Comunica????o TCP/IP com porta
	 * vari??vel. 2 - Comunica????o TCP/IP com porta fixa (Default). 3 - Comunica????o
	 * via modem. 4 ??? Comunica????o via TopPendrive.
	 *
	 * @param Tipo
	 * @return
	 */
	public static native int DefinirTipoConexao(int Tipo);

	/**
	 * Abre a porta de comunica????o. essa fun????o dever?? ser chamada antes de iniciar
	 * qualquer processo de transmiss??o ou recep????o de dados com o Inner. Esta
	 * fun????o deve ser chamada apenas uma vez e no in??cio da comunica????o, e n??o deve
	 * ser chamada para cada Inner. N??mero da porta serial ou TCP/IP: - Para
	 * comunica????o TCP/IP o valor padr??o da porta ?? 3570 (Default). - Para
	 * comunica????o Serial/Modem o valor padr??o da porta ?? 1, COM 1(Default). - Para
	 * a comunica????o TopPendrive o valor ?? 3(Default).
	 *
	 * @param Porta
	 * @return int
	 */
	public static native int AbrirPortaComunicacao(int Porta);

	/**
	 * Fecha a porta de comunica????o previamente aberta, seja ela serial, Modem ou
	 * TCP/IP. Em modo Off-Line normalmente ?? chamada ap??s enviar/receber todos os
	 * dados do Inner. Em modo On-Line ?? chamada somente no ecerramento do software
	 * do software.
	 */
	public static native void FecharPortaComunicacao();

	/**
	 * Define qual padr??o de cart??o ser?? utilizado pelos Inners, padr??o Topdata ou
	 * padr??o livre. O padr??o Topdata de cart??o est?? descrito no manual dos
	 * equipamentos e ?? utilizado somente com os Inners em modo Off-Line. No padr??o
	 * livre todos os d??gitos do cart??o s??o considerados como matr??cula, ele pode
	 * ser utilizado no modo On Line ou no modo Off Line. Ao chamar essa fun????o, a
	 * quantidade de d??gitos ?? setada para 14. 0 - Padr??o Topdata. 1 - Padr??o livre
	 * (Default).
	 *
	 * @param Padrao
	 * @return int
	 */
	public static native int DefinirPadraoCartao(int Padrao);

	/**
	 * Faz com que o Inner emita um bip curto(aviso sonoro).
	 *
	 * @param Inner
	 * @return
	 */
	public static native int AcionarBipCurto(int Inner);

	/**
	 * Faz com que o Inner emita um bip long(aviso sonoro).
	 *
	 * @param Inner
	 * @return
	 */
	public static native int AcionarBipLongo(int Inner);

	/**
	 * Testa a comunica????o com o Inner, tamb??m utilizado para efetuar a conex??o com
	 * o Inner. Para efetuar a conex??o com o Inner, essa fun????o deve ser executada
	 * em um loop at?? retornar 0(zero), executado com sucesso.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int Ping(int Inner);

	/**
	 * Liga a luz emitida pelo display do Inner. Essa fun????o n??o deve ser utilizada
	 * nas catracas.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LigarBackLite(int Inner);

	/**
	 * Desliga a luz emitida pelo display do Inner. Essa fun????o n??o deve ser
	 * utilizada nas catracas.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int DesligarBackLite(int Inner);

	/**
	 * Permite que os dados sejam inseridos no Inner atrav??s do teclado do
	 * equipamento. Habilitando o par??metro ecoar, o teclado ir?? ecoar asteriscos no
	 * display do Inner. Habilita: 0 - Desabilita o teclado (Default). 1 - Habilita
	 * o teclado. Ecoar: 0 ??? Ecoa o que ?? digitado no display do Inner (Default). 1
	 * ??? Ecoa asteriscos no display do Inner.
	 *
	 * @param Habilita
	 * @param Ecoar
	 * @return
	 */
	public static native int HabilitarTeclado(int Habilita, int Ecoar);

	/**
	 * Configura como ir?? funcionar o acionamento(rele) 1 do Inner, e por quanto
	 * tempo ele ser?? acionado. Fun????o: 0 ??? N??o utilizado(Default). 1 ??? Aciona ao
	 * registrar uma entrada ou sa??da. 2 ??? Aciona ao registrar uma entrada. 3 ???
	 * Aciona ao registrar uma sa??da. 4 ??? Est?? conectado a uma sirene(Ver as fun????es
	 * de sirene). 5 ??? Utilizado para a revista de usu??rios(Ver a fun????o
	 * DefinirPorcentagemRevista). 6 ??? Catraca com a sa??da liberada. 7 ??? Catraca com
	 * a entrada liberada 8 ??? Catraca liberada nos dois sentidos. 9 ??? Catraca
	 * liberada nos dois sentidos e a marca????o(registro) ?? gerada de acordo com o
	 * sentido do giro.</param> Tempo - 0 a 50 segundos. 0(Default).
	 *
	 * @param Funcao
	 * @param Tempo
	 * @return
	 */
	public static native int ConfigurarAcionamento1(int Funcao, int Tempo);

	/**
	 * Configura como ir?? funcionar o acionamento(rele) 2 do Inner, e por quanto
	 * tempo ele ser?? acionado. Fun????o: 0 ??? N??o utilizado(Default). 1 ??? Aciona ao
	 * registrar uma entrada ou sa??da. 2 ??? Aciona ao registrar uma entrada. 3 ???
	 * Aciona ao registrar uma sa??da. 4 ??? Est?? conectado a uma sirene(Ver as fun????es
	 * de sirene). 5 ??? Utilizado para a revista de usu??rios(Ver a fun????o
	 * DefinirPorcentagemRevista). 6 ??? Catraca com a sa??da liberada. 7 ??? Catraca com
	 * a entrada liberada 8 ??? Catraca liberada nos dois sentidos. 9 ??? Catraca
	 * liberada nos dois sentidos e a marca????o(registro) ?? gerada de acordo com o
	 * sentido do giro.</param> Tempo - 0 a 50 segundos. 0(Default).
	 *
	 * @param Funcao
	 * @param Tempo
	 * @return
	 */
	public static native int ConfigurarAcionamento2(int Funcao, int Tempo);

	/**
	 * Configura o tipo do leitor que o Inner est?? utilizando, se ?? um leitor de
	 * c??digo de barras, magn??tico ou proximidade. Tipo: 0 ??? Leitor de c??digo de
	 * barras(Default). 1 ??? Leitor Magn??tico. 2 ??? Leitor proximidade AbaTrack2. 3 ???
	 * Leitor proximidade Wiegand e Wiegand Facility Code. 4 ??? Leitor proximidade
	 * Smart Card.
	 *
	 * @param Tipo
	 * @return
	 */
	public static native int ConfigurarTipoLeitor(int Tipo);

	/**
	 * Configura as opera????es que o leitor ir?? executar. Se ir?? registrar os dados
	 * somente como entrada independente do sentido em que o cart??o for passado,
	 * somente como sa??da ou como entrada e sa??da. Opera????o: 0 ??? Leitor
	 * desativado(Default). 1 ??? Somente para entrada. 2 ??? Somente para sa??da. 3 ???
	 * Entrada e sa??da. 4 ??? Entrada e sa??da invertidas
	 *
	 * @param Operacao
	 * @return
	 */
	public static native int ConfigurarLeitor1(int Operacao);

	/**
	 * Configura as opera????es que o leitor ir?? executar. Se ir?? registrar os dados
	 * somente como entrada independente do sentido em que o cart??o for passado,
	 * somente como sa??da ou como entrada e sa??da. Opera????o: 0 ??? Leitor
	 * desativado(Default). 1 ??? Somente para entrada. 2 ??? Somente para sa??da. 3 ???
	 * Entrada e sa??da. 4 ??? Entrada e sa??da invertidas
	 *
	 * @param Operacao
	 * @return
	 */
	public static native int ConfigurarLeitor2(int Operacao);

	/**
	 * Define a quantidade de d??gitos dos cart??es a serem lidos pelo Inner.
	 * Quantidade - 1 a 16 d??gitos. 14(Default).
	 *
	 * @param Quantidade
	 * @return
	 */
	public static native int DefinirQuantidadeDigitosCartao(int Quantidade);

	/**
	 * Configura o Inner para registrar as tentativa de acesso negado. O Inner ir??
	 * rgistrar apenas os acessos negados em rela????o a lista de acesso configurada
	 * para o modo Off-Line, ver as fun????es DefinirTipoListaAcesso e ColetarBilhete.
	 * TipoRegistro: 0 ??? N??o registrar o acesso negado. 1 ??? Apenas o acesso negado.
	 * 2 ??? Falha na valida????o da digital. 3 ??? Acesso negado e falha na valida????o da
	 * digital.
	 *
	 * @param TipoRegistro
	 * @return
	 */
	public static native int RegistrarAcessoNegado(int TipoRegistro);

	/**
	 * Define qual ser?? o tipo do registro realizado pelo Inner ao aproximar um
	 * cart??o do tipo proximidade no leitor do Inner, sem que o usu??rio tenha
	 * pressionado a tecla entrada, sa??da ou fun????o. Funcao: 0 ???
	 * Desablitado(Default). 1 a 9 ??? Registrar como uma fun????o do teclado do Inner.
	 * 10 ??? Registrar sempre como entrada. 11 ??? Registrar sempre como sa??da. 12 ???
	 * Libera a catraca nos dois sentidos e registra o bilhete conforme o sentido
	 * giro.
	 *
	 * @param Funcao
	 * @return
	 */
	public static native int DefinirFuncaoDefaultLeitoresProximidade(int Funcao);

	/**
	 * Habilita os leitores wiegand para o primeiro leitor e o segundo leitor do
	 * Inner, e configura se o segundo leitor ir?? exibir as mensagens configuradas.
	 * Habilita: 0 ??? N??o habilita o segundo leitor como wiegand(Default). 1 ???
	 * Habilita o segundo leitor como wiegand. ExibirMensagem: 0 ??? N??o exibe
	 * mensagem segundo leitor(Default). 1 ??? Exibe mensagem segundo leitor.
	 *
	 * @param Habilita
	 * @param ExibirMensagem
	 * @return
	 */
	public static native int ConfigurarWiegandDoisLeitores(int Habilita, int ExibirMensagem);

	/**
	 * Configura o tipo de registro que ser?? associado a uma marca????o, quando for
	 * inserido o dedo no Inner bio sem que o usu??rio tenha definido se ?? um
	 * entrada, sa??da, fun????o, etc. Funcao: 0 ??? desabilitada(Default). 1 a 9 ???
	 * fun????es de 1 a 9. 10 ??? entrada. 11 ??? sa??da. 12 ??? libera catraca para os dois
	 * lados e registra bilhete conforme o giro.
	 *
	 * @param Funcao
	 * @return
	 */
	public static native int DefinirFuncaoDefaultSensorBiometria(int Funcao);

	/**
	 * Envia o buffer interno da dll que cont??m todas as configura????es das fun????es
	 * anteriores para o Inner, ap??s o envio esse buffer ?? limpo sendo necess??rio
	 * chamar novamentes as fun????es acima para reconfigur??-lo. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarConfiguracoes(int Inner);

	/**
	 * Configura o rel??gio(data/hora) do Inner. Inner: 1 a 32 ??? Para comunica????o
	 * serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para
	 * comunica????o TCP/IP porta fixa. Dia - 1 a 31 Mes - 1 a 12. Ano - 0 a 99 Hora -
	 * 0 a 23 Minuto - 0 a 59 Segundo - 0 a 59
	 *
	 * @param Inner
	 * @param Dia
	 * @param Mes
	 * @param Ano
	 * @param Hora
	 * @param Minuto
	 * @param Segundo
	 * @return
	 */
	public static native int EnviarRelogio(int Inner, int Dia, int Mes, int Ano, int Hora, int Minuto, int Segundo);

	/**
	 * Solicita a vers??o do firmware do Inner e dados como o Idioma, se ?? uma vers??o
	 * especial. Inner: N??mero do Inner desejado. Linha: 01 ??? Inner Plus. 02 ??? Inner
	 * Disk. 03 ??? Inner Verid. 06 ??? Inner Bio. 07 ??? Inner NET. Variacao - Depende da
	 * vers??o, existe somente em vers??es customizadas. VersaoAlta - 00 a 99
	 * VersaoBaixa - 00 a 99 VersaoSufixo - Indica o idioma do firmware: 01 ???
	 * Portugu??s. 02 ??? Espanhol. 03 ??? Ingl??s. 04 ??? Franc??s.
	 *
	 * @param Inner
	 * @param Versao
	 * @return
	 */
	public static native int ReceberVersaoFirmware(int Inner, int[] Versao);

	/**
	 * Apaga o buffer com a lista de hor??rios de acesso e envia automaticamente para
	 * o Inner. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o
	 * TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int ApagarHorariosAcesso(int Inner);

	/**
	 * Insere no buffer da dll um hor??rio de acesso. O Inner possui uma tabela de
	 * 100 hor??rios de acesso, para cada hor??rio ?? poss??vel definir 4 faixas de
	 * acesso para cada dia da semana. Horario - 1 a 100 ??? N??mero da tabela de
	 * hor??rios. DiaSemana - 1 a 7 ??? Dia da semana a qual pertence a faixa de
	 * hor??rio. FaixaDia - 1 a 4 ??? Para cada dia da semana existem 4 faixas de
	 * hor??rio. Hora - 0 a 23 Minuto - 0 a 59
	 *
	 * @param Horario
	 * @param DiaSemana
	 * @param FaixaDia
	 * @param Hora
	 * @param Minuto
	 * @return
	 */
	public static native int InserirHorarioAcesso(int Horario, int DiaSemana, int FaixaDia, int Hora, int Minuto);

	/**
	 * Envia para o Inner o buffer com a lista de hor??rios de acesso, ap??s executar
	 * o comando o buffer ?? limpo pela dll automaticamente. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarHorariosAcesso(int Inner);

	/**
	 * Limpar o buffer com a lista de usu??rios cadastrados e envia automaticamente
	 * para o Inner. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para
	 * comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa.
	 *
	 *
	 * @param Inner
	 * @return
	 */
	public static native int ApagarListaAcesso(int Inner);

	/**
	 * Insere no buffer da dll um usu??rio da lista e a qual hor??rio de acesso ele
	 * est?? associado. Os hor??rios j?? dever??o ter sido cadastrados atrav??s das
	 * fun????es InserirHorarioAcesso e enviados atrav??s da fun????o
	 * EnviarHorariosAcesso para a lista ter o efeito correto. Cartao - 1 a ... ???
	 * Dependo do padr??o de cart??o definido e da quantidade de d??gitos definida.
	 * Horario - 1 a 100 ??? N??mero do hor??rio j?? cadastrado no Inner. 101 ??? Acesso
	 * sempre liberado para o usu??rio. 102 ??? Acesso sempre negado para o usu??rio.
	 *
	 * @param Cartao
	 * @param Horario
	 * @return
	 */
	public static native int InserirUsuarioListaAcesso(String Cartao, int Horario);

	/**
	 * Envia o buffer com os usu??rios da lista de acesso para o Inner, ap??s executar
	 * o comando o buffer ?? limpo pela dll automaticamente. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarListaAcesso(int Inner);

	/**
	 * Define qual tipo de lista(controle) de acesso o Inner vai utilizar. Ap??s
	 * habilitar a lista de acesso ?? necess??rio preencher a lista e os hor??rios de
	 * acesso, verificar os as fun????es de ???Hor??rios de Acesso??? e as fun????es da
	 * ???Lista de Acesso???. Tipo: 0 ??? N??o utilizar a lista de acesso. 1 ??? Utilizar
	 * lista branca(cart??es fora da lista tem o acesso negado). 2 ??? Utilizar lista
	 * negra(bloqueia apenas os cart??es da lista).
	 *
	 * @param Tipo
	 * @return
	 */
	public static native int DefinirTipoListaAcesso(int Tipo);

	/**
	 * Testa comunica????o com o Inner e mant??m o Inner em OnLine quando a mudan??a
	 * autom??tica est?? configurada. Especialmente indicada para a verifica????o da
	 * conex??o em comunica????o TCP/IP.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int PingOnLine(int Inner);

	/**
	 * Insere um hor??rio de toque de sirene e configura em quais dias da semana
	 * esses hor??rio ir??o tocar. ?? poss??vel inserir no m??ximo 100 hor??rios para a
	 * sirene. Hora - 0 a 23 Minuto - 0 a 59 Segunda: 0 ??? Desabilita o toque nesse
	 * dia. 1 ??? Habilita o toque nesse dia. Terca: 0 ??? Desabilita o toque nesse dia.
	 * 1 ??? Habilita o toque nesse dia. Quarta: 0 ??? Desabilita o toque nesse dia. 1 ???
	 * Habilita o toque nesse dia. Quinta: 0 ??? Desabilita o toque nesse dia. 1 ???
	 * Habilita o toque nesse dia. Sexta: 0 ??? Desabilita o toque nesse dia. 1 ???
	 * Habilita o toque nesse dia. Sabado: 0 ??? Desabilita o toque nesse dia. 1 ???
	 * Habilita o toque nesse dia. DomingoFeriado: 0 ??? Desabilita o toque nesse dia.
	 * 1 ??? Habilita o toque nesse dia.
	 *
	 * @param Hora
	 * @param Minuto
	 * @param Segunda
	 * @param Terca
	 * @param Quarta
	 * @param Quinta
	 * @param Sexta
	 * @param Sabado
	 * @param DomingoFeriado
	 * @return
	 */
	public static native int InserirHorarioSirene(int Hora, int Minuto, int Segunda, int Terca, int Quarta, int Quinta,
			int Sexta, int Sabado, int DomingoFeriado);

	/**
	 * Envia o buffer com os hor??rio de sirene cadastrados para o Inner. Ap??s
	 * executar a fun????o o buffer ?? limpo automaticamente pela dll. Inner: 1 a 32 ???
	 * Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel.
	 * 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarHorariosSirene(int Inner);

	/**
	 * Libera a catraca para o usu??rio pode efetuar o giro na catraca em ambos os
	 * sentidos. Em modo On-Line, na fun????o ReceberDadosOnLine o valor retornado no
	 * par??metro ???Complemento??? ser?? do tipo entrada ou sa??da, dependendo do sentido
	 * em que o usu??rio passar pela catraca. Essa fun????o deve ser utilizada somente
	 * com Inners Catraca.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LiberarCatracaDoisSentidos(int Inner);
	/**/

	/**
	 * Libera a catraca no sentido de entrada padr??o do Inner, para o usu??rio poder
	 * efetuar o giro na catraca. Em modo On-Line, na fun????o ReceberDadosOnLine o
	 * valor retornado no par??metro ???Complemento??? ser?? do tipo entrada.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LiberarCatracaEntrada(int Inner);
	/**/

	/**
	 * Libera a catraca no sentido de sa??da padr??o do Inner, para o usu??rio poder
	 * efetuar o giro na catraca. Em modo On-Line, na fun????o ReceberDadosOnLine o
	 * valor retornado no par??metro ???Complemento??? ser?? do tipo sa??da. Essa fun????o
	 * deve ser utilizada somente com Inners Catraca.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LiberarCatracaSaida(int Inner);
	/**/

	/**
	 * Libera a catraca no sentido contr??rio a entrada padr??o do Inner, para o
	 * usu??rio poder efetuar o giro na catraca. Em modo On-Line, na fun????o
	 * ReceberDadosOnLine o valor retornado no par??metro ???Complemento??? ser?? do tipo
	 * sa??da. Essa fun????o deve ser utilizada somente com Inners Catraca.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LiberarCatracaEntradaInvertida(int Inner);
	/**/

	/**
	 * Libera a catraca no sentido contr??rio a sa??da padr??o do Inner, para o usu??rio
	 * poder efetuar o giro na catraca. Em modo On-Line, na fun????o
	 * ReceberDadosOnLine o valor retornado no par??metro ???Complemento??? ser?? do tipo
	 * entrada. Essa fun????o deve ser utilizada somente com Inners Catraca.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LiberarCatracaSaidaInvertida(int Inner);
	/**/

	/**
	 * Prepara o Inner para trabalhar no modo Off-Line, por??m essa fun????o ainda n??o
	 * envia essa informa????o para o equipamento.
	 *
	 * @return
	 */
	public static native int ConfigurarInnerOffLine();
	/**/

	/**
	 * Configura a mensagem a ser exibida quando o usu??rio passar o cart??o no
	 * sentido de entrada do Inner. Caso a mensagem passe de 32 caracteres a DLL ir??
	 * utilizar os primeiros 32 caracteres. O Inner n??o aceita caracteres com
	 * acentua????o, padr??o UNICODE ou padr??o ANSI. O Inner aceita apenas os
	 * caracteres do padr??o ASCII. ExibirData: 0 ??? N??o exibe a data/hora na linha
	 * superior do display. 1 ??? Exibe a data/hora na linha superior do
	 * display(Default). Mensagem: String com a mensagem a ser exibida. Caso esteja
	 * exibindo a data/hora o tamanho da mensagem passa a ser 16 ao inv??s de 32.
	 * Caso seja passado uma string vazia o Inner exibir?? a mensagem em branco no
	 * display. Entrada OK(Default).
	 *
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int DefinirMensagemEntradaOffLine(int ExibirData, String Mensagem);
	/**/

	/**
	 * Configura a mensagem a ser exibida quando o usu??rio passar o cart??o no
	 * sentido de sa??da do Inner. Caso a mensagem passe de 32 caracteres a DLL ir??
	 * utilizar os primeiros 32 caracteres. O Inner n??o aceita caracteres com
	 * acentua????o, padr??o UNICODE ou padr??o ANSI. O Inner aceita apenas os
	 * caracteres do padr??o ASCII. ExibirData: 0 ??? N??o exibe a data/hora na linha
	 * superior do display. 1 ??? Exibe a data/hora na linha superior do
	 * display(Default). Mensagem String com a mensagem a ser exibida. Caso esteja
	 * exibindo a data/hora o tamanho da mensagem passa a ser 16 ao inv??s de 32.
	 * Caso seja passado uma string vazia o Inner exibir?? a mensagem em branco no
	 * display. Entrada OK(Default).
	 *
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int DefinirMensagemSaidaOffLine(int ExibirData, String Mensagem);
	/**/

	/**
	 * Configura a mensagem a ser exibida pelo Inner quando ele estiver ocioso. Caso
	 * a mensagem passe de 32 caracteres a DLL ir?? utilizar os primeiros 32
	 * caracteres. O Inner n??o aceita caracteres com acentua????o, padr??o UNICODE ou
	 * padr??o ANSI. O Inner aceita apenas os caracteres do padr??o ASCII. ExibirData:
	 * 0 ??? N??o exibe a data/hora na linha superior do display. 1 ??? Exibe a data/hora
	 * na linha superior do display(Default). Mensagem: String com a mensagem a ser
	 * exibida. Caso esteja exibindo a data/hora o tamanho da mensagem passa a ser
	 * 16 ao inv??s de 32. Caso seja passado uma string vazia o Inner exibir?? a
	 * mensagem em branco no display. Passe o cart??o(Default).
	 *
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int DefinirMensagemPadraoOffLine(int ExibirData, String Mensagem);
	/**/

	/**
	 * Envia o buffer com todas as mensagens off line configuradas anteriormente,
	 * para o Inner. Ap??s executar a fun????o, o buffer com as mensagens ?? limpo
	 * automaticamente pela dll. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ???
	 * Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarMensagensOffLine(int Inner);
	/**/

	/**
	 * Habilita/Desabilita a mudan????o autom??tica do modo OffLine do Inner para
	 * OnLine e viceversa. Configura o tempo ap??s a comunica????o ser interrompida que
	 * est?? mudan??a ir?? ocorrer. Habilita: 0 ??? Desabilita a mudan??a(Default). 1 ???
	 * Habilita a mudan??a. 2 ??? Habilita a mudan??a autom??tica para o modo OnLine TCP
	 * com Ping, onde o Inner precisa receber o comando PingOnLine para manter-se
	 * OnLine. Tempo - Tempo em segundos para ocorrer a mudan??a. 1 a 50.
	 *
	 * @param Habilita
	 * @param Tempo
	 * @return
	 */
	public static native int HabilitarMudancaOnLineOffLine(int Habilita, int Tempo);

	/**
	 * Configura as formas de entradas de dados para quando o Inner mudar para o
	 * modo Off-Line. Para aplica????es com biometria verifique a pr??xima fun????o
	 * ???DefinirEntradasMudan??aOffLineComBiometria???. Teclado: 0 ??? N??o aceita dados
	 * pelo teclado. 1 ??? Aceita dados pelo teclado. Leitor1: 0 ??? desativado 1 ???
	 * somente para entrada 2 ??? somente para sa??da 3 ??? entrada e sa??da 4 ??? sa??da e
	 * entrada Leitor2: 0 ??? desativado 1 ??? somente para entrada 2 ??? somente para
	 * sa??da 3 ??? entrada e sa??da 4 ??? sa??da e entrada Catraca - 0 ??? reservado para
	 * uso futuro.
	 *
	 * @param Teclado
	 * @param Leitor1
	 * @param Leitor2
	 * @param Catraca
	 * @return
	 */
	public static native int DefinirEntradasMudancaOffLine(int Teclado, int Leitor1, int Leitor2, int Catraca);

	/**
	 * Configura a mensagem padr??o exibido pelo Inner quando entrar para on line
	 * ap??s uma queda para off line. 0 ??? N??o exibe a data/hora na linha superior do
	 * display. 1 ??? Exibe a data/hora na linha superior do display. String com a
	 * mensagem a ser exibida. Caso esteja exibindo a data/hora o tamanho da
	 * mensagem passa a ser 16 ao inv??s de 32. Caso seja passado uma string vazia o
	 * Inner n??o exibir?? a mensagem no display
	 *
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int DefinirMensagemPadraoMudancaOnLine(int ExibirData, String Mensagem);

	/**
	 * Configura a mensagem padr??o a ser exibida pelo Inner quando ele mudar para
	 * Off-line. ExibirData: 0 ??? N??o exibe a data/hora na linha superior do display.
	 * 1 ??? Exibe a data/hora na linha superior do display. Mensagem: String com a
	 * mensagem a ser exibida. Caso esteja exibindo a data/hora o tamanho da
	 * mensagem passa a ser 16 ao inv??s de 32. Caso seja passado uma string vazia o
	 * Inner n??o exibir?? a mensagem no display
	 *
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int DefinirMensagemPadraoMudancaOffLine(int ExibirData, String Mensagem);
	/**/

	/**
	 * Envia o buffer com as configura????es de mudan??a autom??tica do modo OnLine para
	 * OffLine . Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o
	 * TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarConfiguracoesMudancaAutomaticaOnLineOffLine(int Inner);

	/**
	 * Configura a mensagem a ser exibida quando o usu??rio passar o cart??o
	 * utilizando uma das fun????es do Inner(de 0 a 9) e a habilita ou desabilita
	 * essas fun????es. Caso a mensagem passe de 32 caracteres a DLL ir?? utilizar os
	 * primeiros 32 caracteres. O Inner n??o aceita caracteres com acentua????o, padr??o
	 * UNICODE ou padr??o ANSI. O Inner aceita apenas os caracteres do padr??o ASCII.
	 * Mensagem: String com a mensagem a ser exibida. Caso esteja exibindo a
	 * data/hora o tamanho da mensagem passa a ser 16 ao inv??s de 32. Caso seja
	 * passado uma string vazia o Inner n??o exibir?? a mensagem no display. Funcao -
	 * 0 a 9. Habilitada: 0 ??? Desabilita a fun????o do Inner(Default). 1 ??? Habilita a
	 * fun????o do Inner.
	 *
	 * @param Mensagem
	 * @param Funcao
	 * @param Habilitada
	 * @return
	 */
	public static native int DefinirMensagemFuncaoOffLine(String Mensagem, int Funcao, int Habilitada);

	/**
	 * Coleta um bilhete Off-Line que est?? armazenado na mem??ria do Inner, os dados
	 * do bilhete s??o retornados por refer??ncia nos par??metros da fun????o. Ates de
	 * chamar esta fun????o pela primeira vez ?? preciso chamar obrigatoriamente as
	 * fun????es DefinirPadraoCartao e DefinirQuantidadeDigitosCartao nessa ordem para
	 * que o n??mero do cart??o seja calculado de forma correta. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa. Tipo - Tipo da marca????o registrada.
	 * 0 a 9 ??? Fun????es registradas pelo cart??o. 10 ??? Entrada pelo cart??o. 11 ??? Sa??da
	 * pelo cart??o. 12 ??? Tentativa de entrada negada pelo cart??o. 13 ??? Tentativa de
	 * sa??da negada pelo cart??o. 100 a 109 ??? Fun????es registradas pelo teclado. 110 ???
	 * Entrada pelo teclado. 111 ??? Sa??da pelo teclado. 112 ??? Tentativa de entrada
	 * negada pelo teclado. 113 ??? Tentativa de sa??da negada pelo teclado. Dia - 1 a
	 * 31 Mes - 1 a 12 Ano - 0 a 99 Hora - 0 a 23 Minuto - 0 a 59 Cartao - N??mero do
	 * cart??o do usu??rio.
	 *
	 * @param Inner
	 * @param Dados
	 * @param Cartao
	 * @return
	 */
	public static native int ColetarBilhete(int Inner, int[] Dados, StringBuffer Cartao);

	/**
	 * Prepara o Inner para trabalhar no modo On-Line, por??m essa fun????o ainda n??o
	 * envia essa informa????o para o equipamento.
	 *
	 * @return
	 */
	public static native int ConfigurarInnerOnLine();

	/**
	 * Configura o Inner para enviar as informa????es de data/hora nos bilhete on
	 * line, esses dados ser??o retornados nos par??metros da fun????o
	 * ReceberDadosOnLine. Recebe: 0 ??? N??o receber a data/hora do bilhete(Default).
	 * 1 ??? Recebe a data/hora do bilhete.
	 *
	 * @param Recebe
	 * @return
	 */
	public static native int ReceberDataHoraDadosOnLine(int Recebe);

	/**
	 * Envia para o Inner a mensagem padr??o(fixa) que ser?? sempre exibida pelo
	 * Inner. Essa mensagem ?? exibida enquanto o Inner estiver ocioso. Caso a
	 * mensagem passe de 32 caracteres a DLL ir?? utilizar os primeiros 32
	 * caracteres. O Inner n??o aceita caracteres com acentua????o, padr??o UNICODE ou
	 * padr??o ANSI. O Inner aceita apenas os caracteres do padr??o ASCII. Inner: 1 a
	 * 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa. ExibirData: 0 ??? N??o
	 * exibe a data/hora na linha superior do display. 1 ??? Exibe a data/hora na
	 * linha superior do display. Mensagem - String com a mensagem a ser exibida.
	 * Caso esteja exibindo a data/hora o tamanho da mensagem passa a ser 16 ao
	 * inv??s de 32. Caso seja passado uma string vazia o Inner exibir?? a mensagem em
	 * branco no display.
	 *
	 * @param Inner
	 * @param ExibirData
	 * @param Mensagem
	 * @return
	 */
	public static native int EnviarMensagemPadraoOnLine(int Inner, int ExibirData, String Mensagem);

	/**
	 * Configura as formas de entrada de dados do Inner no modo OnLine. Cada vez que
	 * alguma informa????o for recebida no modo OnLine atrav??s da fun????o
	 * ReceberDadosOnLine, a fun????o EnviarFormasEntradasOnLine dever?? ser chamada
	 * novamente para reconfigurar o Inner. Inner: 1 a 32 ??? Para comunica????o serial.
	 * 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para
	 * comunica????o TCP/IP porta fixa QtdeDigitosTeclado - 0 a 20 d??gitos.
	 * EcoTeclado: 0 ??? para n??o 1 ??? para sim 2 ??? ecoar FormaEntrada: 0 ??? n??o aceita
	 * entrada de dados 1 ??? aceita teclado 2 ??? aceita leitura no leitor 1 3 ??? aceita
	 * leitura no leitor 2 4 ??? teclado e leitor 1 5 ??? teclado e leitor 2 6 ??? leitor
	 * 1 e leitor 2 7 ??? teclado, leitor 1 e leitor 2 10 ??? teclado + verifica????o
	 * biom??trica 11 ??? leitor1 + verifica????o biom??trica 12 ??? teclado + leitor1 +
	 * verifica????o biom??trica 13 ??? leitor1 com verifica????o biom??trica + leitor2 sem
	 * verifica????o biom??trica 14 ??? leitor1 com verifica????o biom??trica + leitor2 sem
	 * verifica????o biom??trica + teclado sem verifica????o biom??trica 100 ??? Leitor 1 +
	 * Identifica????o Biom??trica (sem Verifica????o) 101 ??? Leitor 1 + Teclado +
	 * Identifica????o Biom??trica (sem Verifica????o) 102 ??? Leitor 1 + Leitor 2 +
	 * Identifica????o Biom??trica (sem Verifica????o) 103 ??? Leitor 1 + Leitor 2 +
	 * Teclado + Identifica????o Biom??trica (sem Verifica????o) 104 ??? Leitor 1 invertido
	 * + Identifica????o Biom??trica (sem Verifica????o) 105 ??? Leitor 1 invertido +
	 * Teclado + Identifica????o Biom??trica (sem Verifica????o) TempoTeclado - 1 a 50
	 * PosicaoCursorTeclado - 1 a 32
	 *
	 * @param Inner
	 * @param QtdeDigitosTeclado
	 * @param EcoTeclado
	 * @param FormaEntrada
	 * @param TempoTeclado
	 * @param PosicaoCursorTeclado
	 * @return
	 */
	public static native int EnviarFormasEntradasOnLine(int Inner, int QtdeDigitosTeclado, int EcoTeclado,
			int FormaEntrada, int TempoTeclado, int PosicaoCursorTeclado);

	/**
	 * Coleta um bilhete OnLine, caso o usu??rio tenha passado ou digitado algum
	 * cart??o no Inner retorna as informa????es do cart??o nos par??metros da fun????o.
	 * Para que a data/hora do bilhete OnLine seja retornada, o Inner dever?? ter
	 * sido previamente configurado atrav??s da fun????o ReceberDataHoraDadosOnLine.
	 * Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com
	 * porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa. Origem - Origem
	 * dos dados recebidos. 1 ??? via teclado 2 ??? via leitor 1 3 ??? via leitor 2 4 ???
	 * sensor da catraca(obsoleto) 5 ??? fim do tempo de acionamento 6 ??? giro da
	 * catraca Topdata (sensor ??tico) 7 ??? Urna (catraca Millenium) 8 ??? Evento no
	 * Sensor 1 9 ??? Evento no Sensor 2 10 ??? Evento no Sensor 3 Complemento -
	 * Informa????es adicionais sobre os dados recebidos. 0 ??? sa??da (com cart??o) 1 ???
	 * entrada (com cart??o) 35 ??? # via teclado (1?? tecla) 42 ??? * via teclado (1??
	 * tecla) 65 ??? ???Fun????o??? via teclado 66 ??? ???Entrada??? via teclado 67 ??? ???Sa??da??? via
	 * teclado 255 ??? Inseriu todos os d??gitos permitidos pelo teclado. Evento do
	 * Sensor 0/1 ??? N??vel atual do sensor Cartao - N??mero do cart??o recebido. Dia -
	 * 1 a 31. Mes - 1 a 12. Ano - 0 a 99 Hora - 0 a 23 Minuto - 0 a 59 Segundo - 0
	 * a 59
	 *
	 * @param Inner
	 * @param Dados
	 * @param Cartao
	 * @return
	 */
	public static native int ReceberDadosOnLine(int Inner, int[] Dados, StringBuffer Cartao);

	/**
	 * Configura as formas de entrada dos dados quando o Inner voltar para o modo On
	 * Line ap??s uma queda para OffLine. Entrada: 0 ??? N??o aceita entrada de dados. 1
	 * ??? Aceita teclado. 2 ??? Aceita leitor 1. 3 ??? Aceita leitor 2. 4 ??? Teclado e
	 * leitor 1. 5 ??? Teclado e leitor 2. 6 ??? Leitor 1 e leitor 2. 7 ??? Teclado,
	 * leitor 1 e 2. 8 ??? Sensor da catraca.
	 *
	 * @param Entrada
	 * @return
	 */
	public static native int DefinirEntradasMudancaOnLine(int Entrada);

	/**
	 * Configura o teclado para quando o Inner voltar para OnLine ap??s uma queda
	 * para OffLine. Digitos - 0 a 20 d??gitos. EcoDisplay: 0 ??? para n??o ecoar 1 ???
	 * para sim 2 ??? ecoar '*' Tempo - 1 a 50. PosicaoCursor - 1 a 32.
	 *
	 * @param Digitos
	 * @param EcoDisplay
	 * @param Tempo
	 * @param PosicaoCursor
	 * @return
	 */
	public static native int DefinirConfiguracaoTecladoOnLine(int Digitos, int EcoDisplay, int Tempo,
			int PosicaoCursor);

	/**
	 * Habilita/Desabilita a identifica????o biom??trica e/ou a verifica????o biom??trica
	 * do Inner bio. O resultado da configura????o deve ser obtivo atrav??s do comando
	 * ResultadoConfiguracaoBio. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ???
	 * Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa. HabilitaIdentificacao: 0 ??? Desabilita. 1 ??? Habilita.
	 * HabilitaVerificacao: 0 ??? Desabilita. 1 ??? Habilita.
	 *
	 * @param Inner
	 * @param HabilitaIdentificacao
	 * @param HabilitaVerificacao
	 * @return
	 */
	public static native int ConfigurarBio(int Inner, int HabilitaIdentificacao, int HabilitaVerificacao);

	/**
	 * Retorna o resultado da configura????o do Inner Bio, fun????o ConfigurarBio. Se o
	 * retorno for igual a 0 ?? poque o Inner bio foi configurado com sucesso. Inner:
	 * 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando
	 * foi utilizado com o Inner em OffLine. 1 ??? O comando foi utilizado com o Inner
	 * em OnLine.
	 *
	 * @param Inner
	 * @param OnLine
	 * @return
	 */
	public static native int ResultadoConfiguracaoBio(int Inner, int OnLine);

	/**
	 * Insere o n??mero do cart??o na lista de usu??rios sem digital do Inner bio. Este
	 * n??mero ficara armazenado em um buffer interno dentro da dll e somente ser??
	 * enviado para o Inner ap??s a chamada a fun????o
	 * EnviarListaUsuariosSemDigitalBio. O n??mero m??ximo de d??gitos para o cart??o ??
	 * 10, caso os cart??es tenham mais de 10 d??gitos, utilizar os 10 d??gitos menos
	 * significativos do cart??o. Cartao - 1 a 9999999999 ??? N??mero do cart??o do
	 * usu??rio.
	 *
	 * @param Cartao
	 * @return
	 */
	public static native int IncluirUsuarioSemDigitalBio(String Cartao);

	public static native int IncluirUsuarioSemDigitalBioInnerAcesso(String Cartao);

	/**
	 * Solicita a vers??o do firmware da placa do Inner Bio, a placa que armazena as
	 * digitais. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o
	 * TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarVersaoBio(int Inner);

	/**
	 * Envia o buffer com a lista de usu??rios sem digital para o Inner. Ap??s a
	 * execu????o do comando, o buffer ?? limpo pela dll. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int EnviarListaUsuariosSemDigitalBio(int Inner);

	/**
	 * Envia o buffer com a lista de usu??rios sem digital para o Inner. Ap??s a
	 * execu????o do comando, o buffer ?? limpo pela dll. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @param QtdDigitos
	 * @return
	 */
	public static native int EnviarListaUsuariosSemDigitalBioVariavel(int Inner, int QtdDigitos);

	/**
	 * Solicita o modelo do Inner bio. Para receber o resultado dessa opera????o voc??
	 * dever?? chamar a fun????o ReceberModeloBio enquanto o retorno for processando a
	 * opera????o. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o
	 * TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarModeloBio(int Inner);

	/**
	 * Retorna o resultado do comando SolicitarModeloBio, o modelo do Inner Bio ??
	 * retornado por refer??ncia no par??metro da fun????o. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando foi utilizado
	 * com o Inner em OffLine. 1 ??? O comando foi utilizado com o Inner em OnLine
	 * Modelo: 2 ??? Bio Light 100 usu??rios. 4 ??? Bio 1000/4000 usu??rios. 255 ??? Vers??o
	 * desconhecida.
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Modelo
	 * @return
	 */
	public static native int ReceberModeloBio(int Inner, int OnLine, int[] Modelo);

	/**
	 * Retorna o resultado do comando SolicitarVersaoBio, a vers??o do Inner Bio ??
	 * retornado por refer??ncia nos par??metros da fun????o. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando foi utilizado
	 * com o Inner em OffLine. 1 ??? O comando foi utilizado com o Inner em OnLine
	 * VersaoAlta - Parte alta da vers??o. VersaoBaixa - Parte baixa da vers??o.
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Versao
	 * @return
	 */
	public static native int ReceberVersaoBio(int Inner, int OnLine, int[] Versao);

	/**
	 * Define que o Inner utilizado no momento ?? um Inner bio light ao inv??s de um
	 * Inner bio 1000/4000. Essa fun????o dever?? ser chamada sempre que necess??rio
	 * antes das fun????es SolicitarUsuarioCadastradoBio, SolicitarExclusaoUsuario,
	 * InserirUsuarioLeitorBio e FazerVerificacaoBiometricaBio. Light: 1 ??? ?? um
	 * Inner bio light 0 ??? ?? um Inner bio 1000/4000(valor default)
	 *
	 * @param Light
	 */
	public static native void SetarBioLight(int Light);

	/**
	 *
	 * @param habilitado
	 */
	public static native void SetarBioVariavel(int habilitado);

	/**
	 * Solicita para o Inner Bio inserir um usu??rio diretamente pelo leitor
	 * biom??trico. O leitor ir?? acender a luz vermelho e ap??s o usu??rio inserir a
	 * digital, automaticamente o usu??rio ser?? cadastrado no nner bio com o n??mero
	 * do cart??o passado no par??metro Usu??rio. Inner: 1 a 32 ??? Para comunica????o
	 * serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para
	 * comunica????o TCP/IP porta fixa. Tipo: 0 ??? para solicitar o primeiro template 1
	 * ??? para solicitar o segundo template (mesmo dedo) e salvar. 2 ??? para solicitar
	 * o segundo template (outro dedo) e salvar. Usuario - N??mero do cart??o que o
	 * usu??rio ter??.
	 *
	 * @param Inner
	 * @param Tipo
	 * @param Usuario
	 * @return
	 */
	public static native int InserirUsuarioLeitorBio(int Inner, int Tipo, String Usuario);

	/**
	 * Retorna o resultado do comando InserirUsuarioLeitorBio. Se o retorno for
	 * igual a 0 ?? porque o usu??rio foi cadastrado com sucesso. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando foi utilizado
	 * com o Inner em OffLine. 1 ??? O comando foi utilizado com o Inner em OnLine.
	 *
	 * @param Inner
	 * @param OnLine
	 * @return
	 */
	public static native int ResultadoInsercaoUsuarioLeitorBio(int Inner, int OnLine);

	/**
	 * Solicita para o Inner bio excluir o cadastro do usu??rio desejado. O Retorno
	 * da exclus??o ?? verificado atrav??s da fun????o UsuarioFoiExcluido Inner: 1 a 32 ???
	 * Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel.
	 * 1 a ... ??? Para comunica????o TCP/IP porta fixa. Usuario - N??mero do cart??o do
	 * usu??rio cadastrado.
	 *
	 * @param Inner
	 * @param Usuario
	 * @return
	 */
	public static native int SolicitarExclusaoUsuario(int Inner, String Usuario);

	/**
	 * Retorna o resultado do comando SolicitarExclusaoUsuario, se o retorno da
	 * fun????o for igual a 0 ?? porque o usu??rio foi exclu??do com sucesso. Inner: 1 a
	 * 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando
	 * foi utilizado com o Inner em OffLine. 1 ??? O comando foi utilizado com o Inner
	 * em OnLine.
	 *
	 * @param Inner
	 * @param OnLine
	 * @return
	 */
	public static native int UsuarioFoiExcluido(int Inner, int OnLine);

	/**
	 * Solicita do Inner Bio, o template com as duas digitais do Usuario desejado.
	 * Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com
	 * porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa. Usuario -
	 * N??mero do cart??o do usu??rio cadastrado.
	 *
	 * @param Inner
	 * @param Usuario
	 * @return
	 */
	public static native int SolicitarUsuarioCadastradoBio(int Inner, String Usuario);

	/**
	 * Retorna o resultado do comando SolicitarUsuarioCadastradoBio e o template com
	 * as duas digitais do usu??rio cadastrado no Inner Bio. O template ?? retornado
	 * por refer??ncia nos par??metros da fun????o. Inner: 1 a 32 ??? Para comunica????o
	 * serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para
	 * comunica????o TCP/IP porta fixa. OnLine: 0 ??? O comando foi utilizado com o
	 * Inner em OffLine. 1 ??? O comando foi utilizado com o Inner em OnLine Template:
	 * Cadastro do usu??rio com as duas digitais, o dado est?? em bin??rio e n??o deve
	 * ser alterado nunca. O tamanho do template ?? de 844 bytes.
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Template
	 * @return
	 */
	public static native int ReceberUsuarioCadastradoBio(int Inner, int OnLine, byte[] Template);

	/**
	 * Solicita a quantidade de usu??rios cadastrados no Inner Bio.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarQuantidadeUsuariosBio(int Inner);

	/**
	 * Retorna o resultado do comando SolicitarQuantidadeUsuariosBio, a quantidade
	 * de usu??rios cadastrados no Inner Bio ?? retornado por refer??ncia nos
	 * par??metros da fun????o. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para
	 * comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa. OnLine: 0 ??? O comando foi utilizado com o Inner em OffLine. 1 ??? O
	 * comando foi utilizado com o Inner em OnLine Quantidade - Total de usu??rios
	 * cadastrados no Inner Bio.
	 *
	 * @param Inner
	 * @param OnLine
	 * @param Quantidade
	 * @return
	 */
	public static native int ReceberQuantidadeUsuariosBio(int Inner, int OnLine, int[] Quantidade);

	/**
	 * Prepara a dll para iniciar a coleta dos usu??rios do Inner bio, essa fun????o
	 * deve ser chamada obrigatoriamente no in??cio do processo.
	 */
	public static native void InicializarColetaListaUsuariosBio();

	/**
	 * Retorna 1 se ainda existe mais pacotes da lista de usu??rios, a ser recebido
	 * do Inner Bio.
	 *
	 * @return
	 */
	public static native int TemProximoPacote();

	/**
	 * Recebe um usu??rio por vez do pacote recebido anteriormente. O n??mero do
	 * usu??rio ?? retornado pelo par??metro da fun????o. Inner: 1 a 32 ??? Para
	 * comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta vari??vel. 1 a
	 * ... ??? Para comunica????o TCP/IP porta fixa. Usuario - N??mero do cart??o do
	 * usu??rio cadastrado.
	 *
	 * @param Inner
	 * @param Usuario
	 * @return
	 */
	public static native int ReceberUsuarioLista(int Inner, StringBuffer Usuario);

	/**
	 * Retorna 1 se ainda existe usu??rios no pacote recebido da lista.
	 *
	 * @return
	 */
	public static native int TemProximoUsuario();

	/**
	 * Solicita o pacote(a parte) atual da lista de usu??rios do Inner bio. Inner: 1
	 * a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarListaUsuariosBio(int Inner);

	/**
	 * Solicita o pacote(a parte) atual da lista de usu??rios do Inner bio. Inner: 1
	 * a 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int SolicitarListaUsuariosBioVariavel(int Inner);

	/**
	 * Receber o pacote solicitado pela fun????o SolicitarListaUsuariosBio. Inner: 1 a
	 * 32 ??? Para comunica????o serial. 1 a 99 ??? Para comunica????o TCP/IP com porta
	 * vari??vel. 1 a ... ??? Para comunica????o TCP/IP porta fixa.
	 *
	 * @param Inner
	 * @return
	 */
	public static native int ReceberPacoteListaUsuariosBio(int Inner);

	/**
	 * Envia um template com duas digitais para o Inner Bio cadastrar no seu banco
	 * de dados. O resultado do cadastro deve ser verificado no retorno da fun????o
	 * UsuarioFoiEnviado. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ??? Para
	 * comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa. Template: O cadastro do usu??rio j?? contendo as duas digitais e o
	 * n??mero do usu??rio. ?? um array de bytes com o tamanho de 844 bytes.
	 *
	 * @param Inner
	 * @param Template
	 * @return
	 */
	public static native int EnviarUsuarioBio(int Inner, byte[] Template);

	/**
	 * Retorna o resultado do cadastro do Template no Inner Bio, atrav??s da fun????o
	 * EnviarUsuarioBio. Se o retorno for igual a 0 ?? porque o template foi
	 * cadastrado com sucesso. Inner: 1 a 32 ??? Para comunica????o serial. 1 a 99 ???
	 * Para comunica????o TCP/IP com porta vari??vel. 1 a ... ??? Para comunica????o TCP/IP
	 * porta fixa. OnLine: 0 ??? O comando foi utilizado com o Inner em OffLine. 1 ??? O
	 * comando foi utilizado com o Inner em OnLine.
	 *
	 * @param Inner
	 * @param OnLine
	 * @return
	 */
	public static native int UsuarioFoiEnviado(int Inner, int OnLine);

	/**
	 * Configura as formas de entradas de dados para quando o Inner mudar para o
	 * modo Off-Line. Esse comando difere do anterior por permitir a configura????o de
	 * biometria. Atrav??s dessa fun????o o Inner pode ser configurado para trabalhar
	 * com verifica????o ou identifica????o biom??trica, quando ocorrer uma mudan??a
	 * autom??tica de On-Line para Off-Line. Teclado: 0 ??? N??o aceita dados pelo
	 * teclado. 1 ??? Aceita dados pelo teclado. Leitor1: 0 ??? desativado 3 ??? entrada e
	 * sa??da 4 ??? sa??da e entrada (nesse caso for??a Leitor2 igual a zero)</param>
	 * Leitor2: 0 ??? desativado 3 ??? entrada e sa??da Verificacao: 0 ??? desativada 1 ???
	 * ativada Identificacao: 0 ??? desativada 1 ??? ativada
	 *
	 * @param Teclado
	 * @param Leitor1
	 * @param Leitor2
	 * @param Verificacao
	 * @param Identificacao
	 * @return
	 */
	public static native int DefinirEntradasMudancaOffLineComBiometria(int Teclado, int Leitor1, int Leitor2,
			int Verificacao, int Identificacao);

	/**
	 * Liga led vermelho InnerAcesso
	 *
	 * @param Inner
	 * @return
	 */
	public static native int LigarLedVermelho(int Inner);

	/**
	 * Desligar led vermelho InnerAcesso
	 *
	 * @param Inner
	 * @return
	 */
	public static native int DesligarLedVermelho(int Inner);

	/**
	 * M??todo que retorna os segundos do Sistema.
	 *
	 * @return
	 */

	public static native int ConfigurarAjustesSensibilidadeBio(int Inner, byte Ganho, byte Brilho, byte Contraste);

	public static native int ConfigurarAjustesQualidadeBio(int Inner, byte Registro, byte Verificacao);

	public static native int ConfigurarAjustesSegurancaBio(int Inner, byte Identificacao, byte Verificacao);

	public static native int ConfigurarCapturaAdaptativaBio(int Inner, byte Capturar, byte Total, byte Tempo);

	public static native int ConfigurarFiltroBio(int Inner, byte Habilitar);

	public static native int ConfigurarTimeoutIdentificacao(byte TimeoutIdentificacao);

	public static native int ConfigurarNivelLFD(byte NivelLFD);

	public static native int EnviarAjustesBio(int Inner);

	public static native int DefinirMensagemApresentacaoEntrada(int ExibirData, String msg);

	public static native int DefinirMensagemApresentacaoSaida(int ExibirData, String msg);

	public static native int InserirHorarioMudancaEntrada(byte Hora1, byte Minuto1, byte Hora2, byte Minuto2,
			byte Hora3, byte Minuto3);

	public static native int InserirHorarioMudancaSaida(byte Hora1, byte Minuto1, byte Hora2, byte Minuto2, byte Hora3,
			byte Minuto3);

	public static native int EnviarBufferEventosMudancaAuto(int Inner);

	/// Novos comandos vers??o 6xx 4.0.0
	public static native int ReceberVersaoFirmware6xx(int Inner, int[] Versao);

	public static native int ReceberConfiguracoesInner(int Inner, byte[] ConfiguracoesInner);

	public static native int RequisitarQuantidadeUsuariosBio(int Inner, int TipoComBio);

	public static native int RespostaQuantidadeUsuariosBio(int Inner, int[] QtdUsuariosBio);

	public static native int RequisitarListarUsuariosBio(int Inner, int TipoComBio, int NumPacote);

	public static native int RespostaListarUsuariosBio(int Inner, int[] QtdPacotes, int[] Tamanho);

	public static native int ReceberListaPacUsuariosBio(int Inner, byte[] ListaUsuarios, int TamanhoBuffer);

	public static native int EnviarDigitalUsuarioBio(int Inner, int TipoModBio, String Usuario, byte[] Digital1,
			byte[] Digital2);

	public static native int RespostaEnviarDigitalUsuarioBio(int Inner);

	public static native int RequisitarModeloBio(int Inner, int TipoComBio);

	public static native int RespostaModeloBio(int Inner, byte[] ModeloBio);

	public static native int RequisitarVersaoBio(int Inner, int TipoComBio);

	public static native int RespostaVersaoBio(int Inner, byte[] VersaoBio);

	public static native int RequisitarExcluirUsuarioBio(int Inner, int TipoComBio, String Usuario);

	public static native int RespostaExcluirUsuarioBio(int Inner);

	public static native int RequisitarExcluirTodosUsuariosBio(int Inner, int TipoComBio);

	public static native int RespostaExcluirTodosUsuariosBio(int Inner);

	public static native int RequisitarUsuarioCadastradoBio(int Inner, int TipoModBio, String Usuario);

	public static native int RespostaUsuarioCadastradoBio(int Inner, int[] TamanhoReceber);

	public static native int ReceberDigitalUsuarioBio(int Inner, byte[] BufferTemplate, int TamanhoReceber);

	public static native int RequisitarEnviarAjustesBio(int Inner, int TipoModBio, byte[] AjustesBio);

	public static native int RespostaEnviarAjustesBio(int Inner);

	public static native int RequisitarVerificarDigitalBio(int Inner, int TipoModBio, String Usuario);

	public static native int RespostaVerificarDigitalBio(int Inner);

	public static native int RequisitarIdentificarUsuarioLeitorBio(int Inner, int TipoModBio);

	public static native int RespostaIdentificarUsuarioLeitorBio(int Inner, byte[] UsuarioIdentificado);

	public static native int RequisitarReceberTemplateLeitorInnerBio(int Inner, int TipoModBio);

	public static native int RespostaReceberTemplateLeitorInnerBio(int Inner, int[] Tamanho);

	public static native int ReceberTemplateLeitorInnerBio(int Inner, byte[] TemplateRecebido, int Tamanho);

	public static native int RequisitarVerificarCadastroUsuarioBio(int Inner, int TipoModBio, String Usuario);

	public static native int RespostaVerificarCadastroUsuarioBio(int Inner);

	public static native int RequisitarHabilitarIdentificacaoVerificacao(int Inner, int TipoModBio, int Identificacao,
			int Verificacao);

	public static native int RespostaHabilitarIdentificacaoVerificacao(int Inner);

	public static native int LigarLedVerde(int Inner);

	public static native int DesabilitarRele1(int Inner);

	public static native int DesabilitarRele2(int Inner);

	public static native int ReceberDadosOnLineComLetras(int Inner, int[] Dados, StringBuffer Cartao);

	public static native int EnviarSinalizacao(int Inner, int TipoSinalizacao, int ModoSinalizacao, int Terminal);

	public static native int DesabilitarWebServer(int Desabilita);

	public static native int DefinirSensorPortaOffline(int Logica);

	public static native int DesabilitarBipColetor(int Desabilita);

	public static native int ConfigurarBotaoExternoOffline(int Funcao);

	public static native int ReceberVersaoFirmware6xx_ComComplementar(int Inner, int[] Versao);

	public static native int RequisitarCadastrarUsuarioLeitorInnerBio(int Inner, int TipoModBio, byte Solicitacao,
			String Usuario);

	public static native int RespostaCadastrarUsuarioLeitorInnerBio(int Inner);

}
