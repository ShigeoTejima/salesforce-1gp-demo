package org.example.repository.result;

/*
 * example:
 * {"id":"a005D000009uyZBQAY","success":true,"errors":[]}
 */
public record InsertRecordResult(String id, boolean success) {}
