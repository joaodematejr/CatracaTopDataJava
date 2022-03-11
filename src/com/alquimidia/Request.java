package com.alquimidia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.alquimidia.entity.TicketGate;
import com.google.gson.Gson;

public class Request {

	public void post() throws IOException {
		Gson gson = new Gson();
		TicketGate ticketGate = new TicketGate();

		String codTicket = "260551405";
		String innerCatraca = "1";
		ticketGate.setId_catraca(innerCatraca);

		URL url = new URL("https://api.parques.frameticket.com.br/5.0/catraca/consulta-ticket/" + codTicket);
		HttpURLConnection http = (HttpURLConnection) url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		http.setRequestProperty("Accept", "application/json");
		http.setRequestProperty("ft-tokenapp", "de775dc0998cc5efe65903252085dccd");
		http.setRequestProperty("ft-appclient", "b60f2893f52e9f12d31addc8abf9a1c6");

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
				System.out.println("Girar a catraca");
				break;
			case "FAIL":
				System.out.println(ticketGateClass.getMsg());
				break;
			default:
				System.out.println("Tentar novamente");
				break;
			}
		}

		http.disconnect();
	}

}
