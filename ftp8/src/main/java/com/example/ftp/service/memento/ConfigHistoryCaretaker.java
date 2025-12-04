package com.example.ftp.service.memento;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Service
public class ConfigHistoryCaretaker {

    private final Map<Long, Stack<FtpUserMemento>> historyMap = new HashMap<>();

    public void saveSnapshot(Long userId, FtpUserMemento memento) {
        historyMap.putIfAbsent(userId, new Stack<>());
        historyMap.get(userId).push(memento);
    }

    public FtpUserMemento undo(Long userId) {
        if (!historyMap.containsKey(userId) || historyMap.get(userId).isEmpty()) {
            return null;
        }
        return historyMap.get(userId).pop();
    }

    public boolean hasHistory(Long userId) {
        return historyMap.containsKey(userId) && !historyMap.get(userId).isEmpty();
    }
}