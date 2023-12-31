/*
 * ElasticsearchTools
 *
 * Version 1.0.0
 *
 * 2018-12-02
 *
 * Copyright (c) 2018. All rights reserved.
 */
package com.alsam.mdbook_01;

import android.os.AsyncTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



/**
 * Provides a suite of tools to Manage Elasticsearch cloud data.
 * Elasticsearch Servers
 *  * http://cmput301.softwareprocess.es:8080/cmput301f18t01test
 *  * http://cmput301.softwareprocess.es:8080/cmput301f18t01
 *  * http://es2.softwareprocess.ca:8080/cmput301f18t01test
 *  * http://es2.softwareprocess.ca:8080/cmput301f18t01
 *
 * @author Noah Burghardt
 * @see ElasticsearchController
 * @version 1.0.0
 */
public class ElasticSearchTools {

}

    /**
     * Sets all metadata at index/metadata/idlists to empty lists and makes availableID = "0".
     */
//    public static void ResetStringIDs() {
//
//        HashMap<String, ArrayList<String>> idlists = new HashMap<>();
//        String availableID = "0";
//        idlists.put("patientIDs", new ArrayList<String>());
//        idlists.put("caregiverIDs", new ArrayList<String>());
//        idlists.put("problemIDs", new ArrayList<String>());
//        idlists.put("recordIDs", new ArrayList<String>());
//        idlists.put("photoIDs", new ArrayList<String>());
//        idlists.put("availableIDs", new ArrayList<String>());
//
//
//
//        JSONObject IDJSON = new JSONObject(idlists);
//        try {
//            IDJSON.put("availableID", availableID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Index JestID = new Index.Builder(IDJSON).index("cmput301f18t01test").type("metadata")
//                .id("idlists")
//                .build();
//        new jestIndexTask().execute(JestID);
//    }

    /**
     * Sets all metadata at index/metadata/idlists to empty lists and makes availableID = 0.
     */
//    public static void ResetIntIDs(){
//
//
//        HashMap<String, Object> idlists = new HashMap<>();
//        int availableID = 0;
//        idlists.put("patientIDs", new ArrayList<String>());
//        idlists.put("caregiverIDs", new ArrayList<String>());
//        idlists.put("problemIDs", new ArrayList<Integer>());
//        idlists.put("recordIDs", new ArrayList<Integer>());
//        idlists.put("photoIDs", new ArrayList<Integer>());
//        idlists.put("availableIDs", new ArrayList<Integer>());
//
//
//
//        JSONObject IDJSON = new JSONObject(idlists);
//        try {
//            IDJSON.put("availableID", availableID);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Index JestID = new Index.Builder(IDJSON).index("cmput301f18t01test").type("metadata")
//                .id("idlists")
//                .build();
//        new jestIndexTask().execute(JestID);
//    }
//
//    public static void setRecordMapping(String mapping) throws JSONException {
//        JSONObject mappingJSON = new JSONObject(mapping);
//        Index JestID = new Index.Builder(mappingJSON).index("cmput301f18t01test").type("record")
//                .build();
//        new jestIndexTask().execute(JestID);
//    }
//
//    /**
//     * Provides async interface for running jest index tasks.
//     */
//    private static class jestIndexTask extends AsyncTask<Index, Void, DocumentResult> {
//
//        /**
//         * Executes the given index.
//         * @param indices Indexes to be executed.
//         * @return Return value of Index (null if failed for any reason)
//         */
//        @Override
//        protected DocumentResult doInBackground(Index... indices) {
//            for (Index index : indices) {
//                try {
//                    JestClientFactory factory = new JestClientFactory();
//                    factory.setDroidClientConfig(new DroidClientConfig
//                            .Builder("http://cmput301.softwareprocess.es:8080")
//                            .multiThreaded(true)
//                            .defaultMaxTotalConnectionPerRoute(2)
//                            .maxTotalConnection(20)
//                            .build());
//                    JestClient client = factory.getObject();
//                    return client.execute(index);
//                } catch (IOException e) {
//                    return null;
//                }
//            }
//            return null;
//        }
//    }
//}
