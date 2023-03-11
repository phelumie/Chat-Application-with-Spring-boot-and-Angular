package com.ajisegiri.chatapp.dto;


import java.util.Date;

public record Message(String from,String to,String content,Date timestamp) {}
