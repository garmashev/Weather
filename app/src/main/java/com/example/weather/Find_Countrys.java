//package com.example.weather;
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//
//import java.net.URL;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//
///**
// * Created by Елизавета on 02.03.2016.
// */
//class DownloadXML extends AsyncTask<String, Void , Void>{
//    MainActivity activity = new MainActivity();
//    NodeList nodeList;
//    ProgressDialog pDialog;
//    protected void onPreExecute() {
//        super.onPreExecute();
//       // pDialog = new ProgressDialog(MainActivity.this);
//        pDialog.setTitle("Обновляем данные");
//        pDialog.setMessage("Загрузка...");
//        pDialog.setIndeterminate(false);
//        pDialog.show();
//
//    }
//    @Override
//    protected Void doInBackground(String... Url) {
//        try {
//            URL url = new URL(Url[0]);
//            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            Document doc = documentBuilder.parse(new InputSource(url.openStream()));
//            doc.getDocumentElement().normalize();
//            nodeList = doc.getElementsByTagName("fact");
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        for (int temp = 0; temp < nodeList.getLength(); temp++) {
//            Node nNode = nodeList.item(temp);
//            if(nNode.getNodeType() == Node.ELEMENT_NODE){
//                org.w3c.dom.Element eElement = (Element) nNode;
//
//                pDialog.hide();
//                break;
//            }
//        }
//        // super.onPostExecute(aVoid);
//    }
//
//
//    private  static String getNode(String sTag, Element eElement){
//        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
//        Node nValue = (Node) nlList.item(0);
//        return nValue.getNodeValue();
//    }
//}
////°