import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MTGHandler {

    MTGHandler() {
    }

    // TODO: Refactor Code
    public ArrayList<String> findCard(String s) throws IOException {
        String url = "https://api.scryfall.com/cards/search?";

        // encode request
        Map<String, String> param = new HashMap<>();
        param.put("q", s); // query base on name
        param.put("order", "rarity"); // order response
        param.put("dir", "desc"); // specify direction
        String req = this.generateRequest(param);

        url = url.concat(req);
        URL getReq = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) getReq.openConnection();
        int status = con.getResponseCode();
        ArrayList<String> cardFaces = new ArrayList<>();
        // handle errors in HTTP request response
        if (status > 299) {
            cardFaces.add("Card not found");
            return cardFaces;
        }

        // read response
        BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer content = new StringBuffer();
        while ((input = in.readLine()) != null) {
            content.append(input);
        }
        in.close();
        con.disconnect();

        JSONObject response = new JSONObject(content.toString());
        JSONArray cards = response.getJSONArray("data");
        JSONObject card = cards.getJSONObject(0); // get first card from result list
        if (card.getString("layout").equals("transform")) { // handle 2-faced cards
            JSONArray faces = card.getJSONArray("card_faces");
            for (int i = 0 ; i < faces.length(); i++) {
                JSONObject face = faces.getJSONObject(i);
                face = face.getJSONObject("image_uris");
                cardFaces.add(face.getString("normal"));
            }
        } else { // handle normal cards
            card = card.getJSONObject("image_uris");
            cardFaces.add(card.getString("normal"));
        }
        return cardFaces;
    }

    /**
     * Generates a request given the parameters and values
     * @param param Pairs of parameter/value to encode
     * @return encoded HTTP request of the given parameter/value pairs
     * @throws IOException if error occurs encoding
     */
    private String generateRequest(Map<String, String> param) throws IOException{
        String request = "";
        for (Map.Entry<String, String> entry: param.entrySet()) {
            request = request.concat(URLEncoder.encode(entry.getKey(),"UTF-8"));
            request = request.concat("=");
            request = request.concat(URLEncoder.encode(entry.getValue(), "UTF-8"));
            request = request.concat("&");
        }
        return request;
    }
}
