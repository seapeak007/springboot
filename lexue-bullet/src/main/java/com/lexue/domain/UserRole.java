package com.lexue.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by UI03 on 2017/6/26.
 */
@Data
@Entity
public class UserRole {
    @Id
    private long user_id ;
    private int role ;
    private int teacher_id ;
}
