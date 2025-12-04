package com.example.ftp.service.visitor;

import org.springframework.stereotype.Component;

@Component
public class StructurePrintVisitor implements Visitor {
    private StringBuilder report = new StringBuilder();
    private int depth = 0;

    public void reset() {
        report.setLength(0);
        depth = 0;
    }

    @Override
    public void visitFile(MyFile file) {
        indent();
        report.append("- FILE: ").append(file.getName())
                .append(" (").append(file.getSize()).append(" bytes)\n");
    }

    @Override
    public void visitDirectory(MyDirectory directory) {
        indent();
        report.append("+ DIR: ").append(directory.getName()).append("\n");
        depth++;
    }

    private void indent() {
        for (int i = 0; i < depth; i++) {
            report.append("  ");
        }
    }

    public String getOutput() {
        return report.toString();
    }
}