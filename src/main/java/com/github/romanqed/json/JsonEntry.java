package com.github.romanqed.json;

import com.github.romanqed.SolidOperation;

import java.util.Map;

final class JsonEntry {
    EntryType type;
    JsonEntry first;
    JsonEntry second;
    SolidOperation operation;
    Map<String, Object> body;
    Map<String, Object> primitive;
}
