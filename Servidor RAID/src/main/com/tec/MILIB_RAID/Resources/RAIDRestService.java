package main.com.tec.MILIB_RAID.Resources;

// Libraries
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import main.com.tec.MILIB_RAID.domain.RaidController;

/**
 * Class that implements the Web Service for the MILIB project for the RAID
 * This Web Service run on Port 9080
 */
@Path("/raid")
public class RAIDRestService {

    RaidController raid = new RaidController();

    /**
     * Converts the received inputStream to a String for handling the
     * received JSON file
     * @param incomingData Data sent by Client
     * @return Data in String type
     */
    private String inputToString(InputStream incomingData){

        // Setting an String Builder
        StringBuilder inputBuilder = new StringBuilder();

        // Try to read the incomingData in JSON format
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
            String line = null;
            while((line = in.readLine())!= null){
                inputBuilder.append(line);
            }
        } catch (Exception e){
            System.out.println("Error Parsing: -");
        }

        // Returns the Data in String format
        return inputBuilder.toString();
    }

    /**
     *
     * @param ID
     * @param img64
     */
    private void jsonParse(String json, String ID,String img64) throws JSONException {


    }

    /* -----------------------------------------------------------------------------------
                                REGULAR SYNTAX - RAID 5
        ----------------------------------------------------------------------------------*/

    /**
     * It is responsible for inserting new images in the RAID and divide the image logically in
     * each disk (directory file)
     * url: http://ip_addr:port/MILIB_RAID_war_exploded/api/raid/write
     *
     * @param incomingData Receive a JSON that contains the metadata of the image
     * @return Respond with the status of the request
     * @throws JSONException
     */
    @POST
    @Path("/write")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response WRITE(InputStream incomingData) throws JSONException, IOException, ClassNotFoundException {

        // Convert the input in to an String
        String recvData = inputToString(incomingData);

        // Show in console the received data
        System.out.println("[WRITE] Image Received: "+ recvData);

        // In this part the write in disk actions are performed

        String ID = "",img64 = "",user = "";
        JSONObject jsonObject = new JSONObject(recvData);

        // Deserealize and shows the data into console
        ID = jsonObject.getString("ID");
        img64 = jsonObject.getString("img64");
        user = jsonObject.getString("username");

        System.out.println("[WRITE] ID: "+ ID);
        System.out.println("[WRITE] img64: "+ img64);
        System.out.println("[WRITE] User: "+ user);

        raid.WriteCommit(img64,ID+"#",user);

        JSONObject json = new JSONObject();
        json.put("Status","OK");

        System.out.println("[WRITE] Data sent: "+ json.toString());

        // Return HTTP response 200 in case of success
        return Response.status(201).entity(json.toString()).build();
    }

    /**
     * Method in charge of returning a JSON with the requested image from the client, under the
     * criterion of the parameters of the metadata
     * url: http://ip_addr:port/MILIB_RAID_war_exploded/api/raid/seek
     * Receive a json with the information of the requested images
     * @return Returns the requested image and metadata in JSON format
     * @throws JSONException
     */
    @POST
    @Path("/select")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response SEEK(InputStream incomingData) throws JSONException, IOException, ClassNotFoundException {

        String recvData = inputToString(incomingData);

        JSONObject input = new JSONObject(recvData);

        String user = input.getString("username");
        String ID = input.getString("ID");

        System.out.println("[SEEK] User:" + user);
        System.out.println("[SEEK] ID: " + ID);

        String img64 = raid.seekCommit(ID+"#",user);

        JSONObject json = new JSONObject();
        json.put("imgStack", img64);
        System.out.println("[SEEK] Data sent: " + json.toString());

        // Return HTTP response 200 in case of success
        return Response.status(200).entity(json.toString()).build();
    }

    /**
     * Method responsible for removing an image from the disk given its metadata
     * url: http://ip_addr:port/MILIB_RAID_war_exploded/api/raid/delete
     * @param incomingData Receive a json with the information of the image to delete
     * @return Indicates whether the image could be deleted
     * @throws JSONException
     */
    @POST
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response DELETE(InputStream incomingData) throws JSONException, IOException, ClassNotFoundException {

        // Convert the input in String
        String recvData = inputToString(incomingData);

        // Show in console the received data
        System.out.println("[DELETE] Data Received: "+ recvData);

        JSONObject input = new JSONObject(recvData);
        String user = input.getString("username");
        String ID = input.getString("ID");

        System.out.println("[DELETE] User:" + user);
        System.out.println("[DELETE] ID: " + ID);

        // In this part the deletion is effected

        raid.deleteCommmit(ID+"#",user);

        // If deletion done, the send Status: OK else Status: FAIL
        JSONObject json = new JSONObject();
        json.put("Status","OK");

        System.out.println("[DELETE] Data sent: "+ json.toString());

        // Return HTTP response 200 in case of success
        return Response.status(200).entity(json.toString()).build();
    }

    /* -----------------------------------------------------------------------------------
                          COMMIT & ROLLBACK - RAID 5
        ----------------------------------------------------------------------------------*/

    /**
     * Make commit of all changes made to the database
     * url: http://ip_addr:port/MILIB_RAID_war_exploded/api/raid/commit
     * @return Returns a text indicating if the commit was successful
     */
    @POST
    @Path("/commit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response COMMIT(InputStream incomingData) throws IOException, JSONException, ClassNotFoundException {

        //Commit stuff here
        // Convert the input in String
        String recvData = inputToString(incomingData);

        // Show in console the received data
        System.out.println("[COMMIT] Data Received: "+ recvData);

        JSONObject input = new JSONObject(recvData);
        String user = input.getString("username");
        System.out.println("[COMMIT] User:" + user);

        raid.commit(user);

        System.out.println("[COMMIT] Commit Request");
        String ans = " RAID Commit Successful!";

        return Response.status(200).entity(ans).build();
    }

    /**
     * Make rollback of all changes made to the database
     * url: http://ip_addr:port/MILIB_RAID_war_exploded/api/raid/back
     * @return Returns a text indicating if the rollback was successful
     */
    @POST
    @Path("/back")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response BACK(InputStream incomingData) throws JSONException {

        // Convert the input in String
        String recvData = inputToString(incomingData);

        // Show in console the received data
        System.out.println("[BACK] Data Received: "+ recvData);

        JSONObject input = new JSONObject(recvData);
        String user = input.getString("username");
        System.out.println("[BACK] User:" + user);

        raid.rollback(user);

        //Commit stuff here
        System.out.println("[BACK] Rollback Request");
        String ans = "RAID Rollback Success!";

        return Response.status(200).entity(ans).build();
    }
}
