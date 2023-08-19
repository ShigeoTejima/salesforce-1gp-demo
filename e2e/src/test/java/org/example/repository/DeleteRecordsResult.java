package org.example.repository;

import java.util.List;

/*
 * example:
 * [{"id":"a005D000009uyaOQAQ","success":true,"errors":[]},{"id":"a005D000009uyaTQAQ","success":true,"errors":[]},{"id":"a005D000009uyaYQAQ","success":true,"errors":[]},{"id":"a005D000009uyadQAA","success":true,"errors":[]}]
 */
public record DeleteRecordsResult(List<DeleteRecordResult> records) {}
