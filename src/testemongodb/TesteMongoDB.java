/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testemongodb;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;

/**
 *
 * @author gustavo
 */
public class TesteMongoDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException {
        User user = new User();
        user.setId(2);
        user.setName("Pankaj");
        user.setEmployee(true);
        user.setRole("CEO");
        
        DBObject doc = createDBObject(user);

        MongoClient mongo = new MongoClient("localhost", 27017);
        mongo.dropDatabase("journaldev");
        
        DB db = mongo.getDB("journaldev");
        
        DBCollection col = db.getCollection("users");

        System.out.println("######################################\ndbNames: " + mongo.getDatabaseNames());
        System.out.println("######################################\ncollectionNames: " + db.getCollectionNames());
        
        //create user
        WriteResult result = col.insert(doc);
        System.out.println("Inserting Collection");
        System.out.println("UpsertedId: " + result.getUpsertedId());
        System.out.println("N: " + result.getN());
        System.out.println("UpdateOfExisting: " + result.isUpdateOfExisting());
//        System.out.println(result.getLastConcern());

        //read example
        DBObject query = BasicDBObjectBuilder.start().add("_id", user.getId()).get();
        DBCursor cursor = col.find(query);
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }

        //update example
        user.setName("Pankaj Kumar");
        doc = createDBObject(user);
        result = col.update(query, doc);
        System.out.println("\nUpdating Collection");
        System.out.println("UpsertedId: " + result.getUpsertedId());
        System.out.println("N: " + result.getN());
        System.out.println("UpdateOfExisting: " + result.isUpdateOfExisting());
//        System.out.println(result.getLastConcern());

        //delete example
        result = col.remove(query);
        System.out.println("\nRemoving Query");
        System.out.println("UpsertedId: " + result.getUpsertedId());
        System.out.println("N: " + result.getN());
        System.out.println("UpdateOfExisting: " + result.isUpdateOfExisting());
//        System.out.println(result.getLastConcern());

        System.out.println("######################################\ndbNames: " + mongo.getDatabaseNames());
        System.out.println("######################################\ncollectionNames: " + db.getCollectionNames());

        //close resources
        mongo.close();
    }

    private static DBObject createDBObject(User user) {
        BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();

        docBuilder.append("_id", user.getId());
        docBuilder.append("name", user.getName());
        docBuilder.append("role", user.getRole());
        docBuilder.append("isEmployee", user.isEmployee());
        return docBuilder.get();
    }

}
