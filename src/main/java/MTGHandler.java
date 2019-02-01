import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class MTGHandler {

    MTGHandler() {
    }

    // TODO: Refactor Code
    public ArrayList<String> findCard(String s) throws IOException {
        String url = "https://api.scryfall.com/cards/search?";

        // encode request
        // TODO: write helper to handle dynamic parameters
        String req = "";
        req = req.concat(URLEncoder.encode("q", "UTF-8"));
        req = req.concat("=");
        req = req.concat(URLEncoder.encode(s, "UTF-8"));
        req = req.concat("&");
        req = req.concat(URLEncoder.encode("order", "UTF-8"));
        req = req.concat("=");
        req = req.concat(URLEncoder.encode("rarity", "UTF-8"));
        req = req.concat("&");
        req = req.concat(URLEncoder.encode("dir", "UTF-8"));
        req = req.concat("=");
        req = req.concat(URLEncoder.encode("desc", "UTF-8"));

        url = url.concat(req);
        URL getReq = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) getReq.openConnection();
        System.out.println("connection established");
        int status = con.getResponseCode();
        System.out.println("Status code: " + status);

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
}
