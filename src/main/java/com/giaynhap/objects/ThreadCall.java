package com.giaynhap.objects;

import com.giaynhap.model.Users;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ThreadCall implements Closeable {
    private final Logger log = LoggerFactory.getLogger(ThreadCall.class);

    private final ConcurrentMap<String, UserSession> participants = new ConcurrentHashMap<>();
    private  MediaPipeline pipeline;
    private  String name;

    public ThreadCall(String roomName, MediaPipeline pipeline) {
        this.name = roomName;
        this.pipeline = pipeline;
        log.info("ROOM {} has been created", roomName);
    }


    public UserSession join(Users user, WebSocketSession session) throws IOException {

        final UserSession participant = new UserSession(user, session, this.pipeline);
        joinRoom(participant);
        participants.put(participant.getUUID(), participant);
        sendParticipantNames(participant);
        return participant;
    }


    public void leave(UserSession user) throws IOException {
        log.debug("PARTICIPANT {}: Leaving room {}", user.getName(), this.name);
        this.removeParticipant(user.getUUID());
        user.close();
    }

    private Collection<String> joinRoom(UserSession newParticipant) throws IOException {
        final JsonObject newParticipantMsg = new JsonObject();
        newParticipantMsg.addProperty("id", "newParticipantArrived");
        newParticipantMsg.addProperty("uuid", newParticipant.getUUID());

        final List<String> participantsList = new ArrayList<>(participants.values().size());
        log.debug("ROOM {}: notifying other participants of new participant {}", name,
                newParticipant.getName());

        for (final UserSession participant : participants.values()) {
            try {
                participant.sendMessage(newParticipantMsg);
            } catch (final IOException e) {
                log.debug("ROOM {}: participant {} could not be notified", name, participant.getName(), e);
            }
            participantsList.add(participant.getUUID());
        }

        return participantsList;
    }

    private void removeParticipant(String uuid) throws IOException {
        participants.remove(uuid);

        log.debug("ROOM {}: notifying all users that {} is leaving the room", this.name, uuid);

        final List<String> unnotifiedParticipants = new ArrayList<>();
        final JsonObject participantLeftJson = new JsonObject();
        participantLeftJson.addProperty("id", "participantLeft");
        participantLeftJson.addProperty("uuid", uuid);
        for (final UserSession participant : participants.values()) {
            try {
                participant.cancelVideoFrom(uuid);
                participant.sendMessage(participantLeftJson);
            } catch (final IOException e) {
                unnotifiedParticipants.add(participant.getUUID());
            }
        }

        if (!unnotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.name,
                    unnotifiedParticipants, uuid);
        }

    }

    public void sendParticipantNames(UserSession user) throws IOException {

        final JsonArray participantsArray = new JsonArray();
        for (final UserSession participant : this.getParticipants()) {
            if (!participant.equals(user)) {
                final JsonElement participantName = new JsonPrimitive(participant.getUUID());
                participantsArray.add(participantName);
            }
        }

        final JsonObject existingParticipantsMsg = new JsonObject();
        existingParticipantsMsg.addProperty("id", "existingParticipants");
        existingParticipantsMsg.add("data", participantsArray);
        log.debug("PARTICIPANT {}: sending a list of {} participants", user.getName(),
                participantsArray.size());
        user.sendMessage(existingParticipantsMsg);
    }

    public Collection<UserSession> getParticipants() {
        return participants.values();
    }

    public UserSession getParticipant(String uuid) {
        return participants.get(uuid);
    }


    @PreDestroy
    private void shutdown() {
        this.close();
    }


    @Override
    public void close() {

    }

    public String getName(){
        return this.name;
    }
}
