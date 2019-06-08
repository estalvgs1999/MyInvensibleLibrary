package main.com.tec.MILIB_DB.util;

// Libraries
import org.json.*;
import java.util.ArrayList;

/**
 * Class responsible for the translation of the JSON according to the method
 * by which the request was made
 * @author Esteban Alvarado Vargas
 * @version alpha 1.1
 */
public class jsonParser {

    /**
     * It is responsible for converting the entry json into the data required
     * for the metadata insertion
     * @param json Json file to parse
     * @param slotsList List that will store the Slots
     * @param valuesList List that will store the Slots values
     * @throws JSONException If an error is detected when the JSON in parsed
     */
    public static void jsonInsertParser(String json,
                                        ArrayList<String> slotsList,
                                        ArrayList<String> valuesList) throws JSONException{

        // Step 1: First a JSONObject is created and the JSONArray is extracted from it
        JSONObject jsonObject = new JSONObject(json);
        JSONArray slots = jsonObject.getJSONArray("slots");
        JSONArray slotValues = jsonObject.getJSONArray("slotsValues");

        // Step 2: Then the data of the JSONArray is transferred to the lists

        for(int i = 0; i < slots.length(); i++){
            slotsList.add((String)slots.get(i));
            valuesList.add((String)slotValues.get(i));
        }

    }

    /**
     * It is responsible for converting the entry json into the data required
     * for the select of metadata
     * @param json Json file to parse
     * @param slotsList List that will store the Slots to return
     * @param whereList List that will store the Where slots
     * @param valuesList List that will store the Where values
     * @throws JSONException If an error is detected when the JSON in parsed
     */
    public static void jsonSelectParser(String json,
                                        ArrayList<String> slotsList,
                                        ArrayList<String> whereList,
                                        ArrayList<String> valuesList) throws JSONException{

        // Step 1: First a JSONObject is created and the JSONArray is extracted from it
        JSONObject jsonObject = new JSONObject(json);
        JSONArray slots = jsonObject.getJSONArray("slots"); // The slot to return
        JSONArray where = jsonObject.getJSONArray("where"); // The condition
        JSONArray whereValues= jsonObject.getJSONArray("whereValues"); // The values of the condition

        // Step 2: Then the data of the JSONArray is transferred to the lists

        for(int i = 0; i < slots.length(); i++) {
            slotsList.add((String) slots.get(i));
        }

        for(int i = 0; i < where.length(); i++){
            whereList.add((String)where.get(i));
            valuesList.add((String)whereValues.get(i));
        }
    }

    /**
     * It is responsible for converting the entry json into the data required
     * for the update of metadata
     * @param json Json file to parse
     * @param slotsList List that will store the Slots to update
     * @param valuesList List that will store the Slots new values
     * @param whereList List that will store the Where slots
     * @param whereValuesList List that will store the Where Values
     * @throws JSONException If an error is detected when the JSON in parsed
     */
    public static void jsonUpdateParser(String json,
                                        ArrayList<String> slotsList,
                                        ArrayList<String> valuesList,
                                        ArrayList<String> whereList,
                                        ArrayList<String> whereValuesList) throws JSONException{

        // Step 1: First a JSONObject is created and the JSONArray is extracted from it
        JSONObject jsonObject = new JSONObject(json);
        JSONArray slots = jsonObject.getJSONArray("slots"); // The slot to return
        JSONArray slotsValues = jsonObject.getJSONArray("slotsValues");
        JSONArray where = jsonObject.getJSONArray("where");
        JSONArray whereValues = jsonObject.getJSONArray("whereValues");

        // Step 2: Then the data of the JSONArray is transferred to the lists

        for(int i = 0; i < slots.length(); i++) {
            slotsList.add((String) slots.get(i));
            valuesList.add((String) slotsValues.get(i));
        }

        for(int i = 0; i < where.length(); i++){
            whereList.add((String)where.get(i));
            whereValuesList.add((String)whereValues.get(i));
        }

    }

    /**
     * It is responsible for converting the entry json into the data required
     * for the deletion of metadata
     * @param json Json file to parse
     * @param whereList List that will store the Where slots
     * @param whereValuesList List that will store the Where Values
     * @throws JSONException If an error is detected when the JSON in parsed
     */
    public static void jsonDeleteParser(String json,
                                        ArrayList<String> whereList,
                                        ArrayList<String> whereValuesList) throws JSONException {

        // Step 1: First a JSONObject is created and the JSONArray is extracted from it
        JSONObject jsonObject = new JSONObject(json);
        JSONArray where = jsonObject.getJSONArray("where"); // The condition
        JSONArray whereValues = jsonObject.getJSONArray("whereValues");

        // Step 2: Then the data of the JSONArray is transferred to the lists

        for (int i = 0; i < where.length(); i++) {
            whereList.add((String) where.get(i));
            whereValuesList.add((String) whereValues.get(i));
        }
    }

}