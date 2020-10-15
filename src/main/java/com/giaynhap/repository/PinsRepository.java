package com.giaynhap.repository;

import com.giaynhap.model.Conversation;
import com.giaynhap.model.Message;
import com.giaynhap.model.Pins;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PinsRepository extends CrudRepository<Pins, String> {


}

