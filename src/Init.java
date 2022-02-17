import com.topdata.EasyInner;

public class Init {
		
	static boolean parar = false;

	public static void main(String[] args) throws InterruptedException {
		String arch = System.getProperty("sun.arch.data.model");
		
		if (arch.equalsIgnoreCase("32")) {
			startMachine();
		} else {
			System.err.println("\nThis project only works on x86");
		}	
	}

	private static void startMachine() throws InterruptedException {
		Integer ret = 0;
		EasyInner.FecharPortaComunicacao();
		EasyInner.DefinirTipoConexao(2);
		ret = EasyInner.AbrirPortaComunicacao(3570);
		if (ret == EasyInner.RET_COMANDO_OK) {
			System.out.println("Porta Aberta");
			machine();
		} else {
			System.err.println("\nErro ao tentar abrir a porta de comunicação.");
		}
	}

	
	private static void machine() throws InterruptedException{
		//EasyInner.ConfigurarInnerOffLine();
		EasyInner.ConfigurarInnerOnLine();
		EasyInner.DefinirPadraoCartao(1);
		EasyInner.ConfigurarAcionamento1(9, 15);
		EasyInner.ConfigurarAcionamento2(9, 15);
		EasyInner.ConfigurarTipoLeitor(7);
				
	}

}
