import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileName);
            Node staff = document.getDocumentElement();
            NodeList employeeList = staff.getChildNodes();

            for (int i = 0; i < employeeList.getLength(); i++) {
                Node employee = employeeList.item(i);
                if (Node.ELEMENT_NODE == employee.getNodeType()) {
                    System.out.println(employee.getNodeName());
                    NodeList parameterList = employee.getChildNodes();

                    long id = 0;
                    String firstName = null;
                    String lastName = null;
                    String country = null;
                    int age = 0;
                    for (int a = 0; a < parameterList.getLength(); a++) {
                        Node parameter = parameterList.item(a);
                        if (Node.ELEMENT_NODE == parameter.getNodeType()) {
                            switch (parameterList.item(a).getNodeName()) {
                                case "id":
                                    id = Long.parseLong(String.valueOf(parameterList.item(a).getChildNodes().item(0).getNodeValue()));
                                    break;
                                case "firstName":
                                    firstName = String.valueOf(parameterList.item(a).getChildNodes().item(0).getNodeValue());
                                    break;
                                case "lastName":
                                    lastName = String.valueOf(parameterList.item(a).getChildNodes().item(0).getNodeValue());
                                    break;
                                case "country":
                                    country = String.valueOf(parameterList.item(a).getChildNodes().item(0).getNodeValue());
                                    break;
                                case "age":
                                    age = Integer.parseInt(String.valueOf(parameterList.item(a).getChildNodes().item(0).getNodeValue()));
                                    break;
                            }
                        }
                    }
                    list.add(new Employee(id, firstName, lastName, country, age));

                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json) {
        try (FileWriter writer = new FileWriter("data.json")) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {

        String fileName = "data.xml";

        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeString(json);
    }
}
