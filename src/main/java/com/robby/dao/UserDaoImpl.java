package com.robby.dao;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.robby.entity.User;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import org.apache.commons.collections.IteratorUtils;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author Robby
 */
public class UserDaoImpl extends Observable {

    public List<User> getAllUsers() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test_db");
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        mongoDatabase = mongoDatabase.withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        FindIterable<User> iterable = mongoCollection.find(User.class);
        Iterator<User> iterator = iterable.iterator();
        mongoClient.close();
        return IteratorUtils.toList(iterator);
    }

    public void addUser(User user) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test_db");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        Document document = new Document(User.UserColumn.COL_M_ID, user.getM_id())
                .append(User.UserColumn.COL_NAME, user.getName())
                .append(User.UserColumn.COL_ROLE, user.getRole());
        mongoCollection.insertOne(document);
        mongoClient.close();
        setChanged();
        notifyObservers(this);
    }

    public void updateUser(User user) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test_db");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        mongoCollection.updateOne(Filters.eq(User.UserColumn.COL_ID, user.getId()),
                Updates.combine(Updates.set(User.UserColumn.COL_M_ID, user.getM_id()),
                        Updates.set(User.UserColumn.COL_NAME, user.getName()),
                        Updates.set(User.UserColumn.COL_ROLE, user.getRole())));
        mongoClient.close();
        setChanged();
        notifyObservers(this);
    }

    public void deleteUser(User user) {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test_db");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("user");
        mongoCollection.deleteOne(Filters.eq(User.UserColumn.COL_ID, user.getId()));
        mongoClient.close();
        setChanged();
        notifyObservers(this);
    }
}
