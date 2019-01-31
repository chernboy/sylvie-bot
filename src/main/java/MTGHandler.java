import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class MTGHandler {
    private URL getReq;

    public MTGHandler() {
    }

    public String findCard(String s) throws IOException {
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
        getReq = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) getReq.openConnection();
        System.out.println("connection established");

        // read response
        BufferedReader in =  new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuffer content = new StringBuffer();
        while ((input = in.readLine()) != null) {
            content.append(input);
        }
        // TODO: Check status code, handle bad requests
        // TODO: Implement functionality for flip cards: layout: transform, card_faces: image_uris
        System.out.println("response read");
        in.close();
        con.disconnect();
        JSONObject response = new JSONObject(content.toString());
        JSONArray cards = response.getJSONArray("data");
        JSONObject card = cards.getJSONObject(0); // get first card from result list
        card = card.getJSONObject("image_uris");
        return card.getString("normal");
    }


}
