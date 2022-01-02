package com.github.xpenatan.gdx.html5.bullet.codegen;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.utils.PositionUtils;
import com.github.xpenatan.gdx.html5.bullet.codegen.util.CustomFileDescriptor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/** @author xpenatan */
public class CodeGen {

    static String gen = "-------------------------------------------------------\n"
            + " * This file was generated by gdx-html5-tools\n"
            + " * Version 1.0.0\n"
            + " *\n * Do not make changes to this file\n"
            + " *-------------------------------------------------------";

    CustomFileDescriptor sourceDir;
    CustomFileDescriptor genDir;
    String[] excludes;
    CodeGenWrapper wrapper;

    public void generate(String sourceDir, String genDir, CodeGenWrapper wrapper, String[] excludes) throws Exception {
        this.excludes = excludes;
        this.wrapper = wrapper;
        this.sourceDir = new CustomFileDescriptor(sourceDir);
        this.genDir = new CustomFileDescriptor(genDir);

        // check if source directory exists
        if(!this.sourceDir.exists()) {
            throw new Exception("Java source directory '" + sourceDir + "' does not exist");
        }

        if(!this.genDir.exists()) {
            if(!this.genDir.mkdirs()) {
                throw new Exception("Couldn't create directory '" + genDir + "'");
            }
        }
        else {
            this.genDir.deleteDirectory();
            if(!this.genDir.mkdirs()) {
                throw new Exception("Couldn't create directory '" + genDir + "'");
            }
        }
        System.out.println("***** GENERATING CODE *****");
        processDirectory(this.sourceDir);
        System.out.println("********** DONE ***********");
    }

    private void processDirectory(CustomFileDescriptor dir) throws Exception {
        CustomFileDescriptor[] files = dir.list();
        for(CustomFileDescriptor file : files) {
            if(file.isDirectory()) {
                if(file.path().contains(".svn")) continue;
                if(file.path().contains(".git")) continue;
                processDirectory(file);
            }
            else {
                if(file.extension().equals("java")) {
                    boolean stop = false;
                    if(excludes != null) {
                        for(int i = 0; i < excludes.length; i++) {
                            String path = file.path();
                            String exclude = excludes[i];

                            if(exclude.startsWith("!")) {
                                String substring = exclude.substring(1);
                                if(path.contains(substring)) {
                                    stop = false;
                                    break;
                                }
                            }
                            else if(path.contains(exclude)) {
                                stop = true;
                            }
                        }
                    }

                    if(stop)
                        continue;

                    String className = getFullyQualifiedClassName(file);
                    CustomFileDescriptor codeFile = new CustomFileDescriptor(genDir + "/" + className + ".cpp");
                    if(file.lastModified() < codeFile.lastModified()) {
                        System.out.println("Code for '" + file.path() + "' up to date");
                        continue;
                    }
                    String javaContent = file.readString();
                    System.out.println("Parsing: " + file);
                    String codeParsed = parseClass(javaContent);
                    generateFile(file, codeParsed);
                }
            }
        }
    }

    private void generateFile(CustomFileDescriptor fileName, String javaContent) {
        String packageFilePath = fileName.file().getAbsolutePath().replace(sourceDir.file().getAbsolutePath(), "");
        String fullPath = genDir.file().getAbsolutePath() + packageFilePath;
        CustomFileDescriptor fileDescriptor = new CustomFileDescriptor(fullPath);
        fileDescriptor.writeString(javaContent, false);
    }

    private String getFullyQualifiedClassName(CustomFileDescriptor file) {
        String className = file.path().replace(sourceDir.path(), "").replace('\\', '.').replace('/', '.').replace(".java", "");
        if(className.startsWith(".")) className = className.substring(1);
        return className;
    }

    private String parseClass(String javaContent) throws Exception {
        CompilationUnit unit = StaticJavaParser.parse(new ByteArrayInputStream(javaContent.getBytes()));

        List<Node> childrenNodes = unit.getChildNodes();
        Iterator<Node> iterator = childrenNodes.iterator();
        ArrayList<CodeGenParserItem> blockCommentItems = new ArrayList<>();

        while(iterator.hasNext()) {
            Node node = iterator.next();

            if(node instanceof PackageDeclaration) {
                PackageDeclaration packageD = (PackageDeclaration) node;
                packageD.setComment(new BlockComment(gen));
            }
            else if(node instanceof ImportDeclaration) {
            }
            else if(node instanceof ClassOrInterfaceDeclaration) {
                parseClassInterface((ClassOrInterfaceDeclaration) node, 0, blockCommentItems);
            }
            else if(node instanceof BlockComment) {
                BlockComment blockComment = (BlockComment) node;
            }
        }

        for(int i = 0; i < blockCommentItems.size(); i++) {
            CodeGenParserItem parserItem = blockCommentItems.get(i);
            parseItem(parserItem);
        }

        return unit.toString();
    }

    private static void parseClassInterface(ClassOrInterfaceDeclaration clazzInterface, int classLevel, ArrayList<CodeGenParserItem> blockCommentItems) {
        ArrayList<Node> array = new ArrayList<>();
        ArrayList<BlockComment> blockComments = new ArrayList<>();
        array.addAll(clazzInterface.getChildNodes());
        PositionUtils.sortByBeginPosition(array, false);

        for(int i = 0; i < array.size(); i++) {
            Node node = array.get(i);

            if(node instanceof BlockComment) {
                BlockComment blockComment = (BlockComment) node;
                blockComments.add(blockComment);
            }
            else if(node instanceof FieldDeclaration) {
                FieldDeclaration field = (FieldDeclaration) node;
                Optional<Comment> optionalComment = field.getComment();
                if(optionalComment.isPresent() && optionalComment.get() instanceof BlockComment) {
                    BlockComment blockComment = (BlockComment) optionalComment.get();
                    blockComments.add(blockComment);
                }
                if(blockComments.size() > 0) {
                    CodeGenParserItem parserItem = new CodeGenParserItem();
                    parserItem.rawComments.addAll(blockComments);
                    parserItem.fieldDeclaration = field;
                    blockComments.clear();
                    blockCommentItems.add(parserItem);
                }
            }
            else if(node instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) node;
                Optional<Comment> optionalComment = method.getComment();
                if(optionalComment.isPresent() && optionalComment.get() instanceof BlockComment) {
                    BlockComment blockComment = (BlockComment) optionalComment.get();
                    blockComments.add(blockComment);
                }
                if(blockComments.size() > 0) {
                    CodeGenParserItem parserItem = new CodeGenParserItem();
                    parserItem.rawComments.addAll(blockComments);
                    parserItem.methodDeclaration = method;
                    blockComments.clear();
                    blockCommentItems.add(parserItem);
                }
            }
            else {
                if(blockComments.size() > 0) {
                    CodeGenParserItem parserItem = new CodeGenParserItem();
                    parserItem.rawComments.addAll(blockComments);
                    blockComments.clear();
                    blockCommentItems.add(parserItem);
                }
                if(node instanceof ClassOrInterfaceDeclaration) {
                    parseClassInterface((ClassOrInterfaceDeclaration) node, ++classLevel, blockCommentItems);
                }
            }
        }

        if(classLevel == 0 && blockComments.size() > 0) {
            CodeGenParserItem parserItem = new CodeGenParserItem();
            parserItem.rawComments.addAll(blockComments);
            blockComments.clear();
            blockCommentItems.add(parserItem);
        }
    }

    private void parseItem(CodeGenParserItem item) {

        for(int i = 0; i < item.rawComments.size(); i++) {
            BlockComment rawBlockComment = item.rawComments.get(i);
            rawBlockComment.remove();
        }

        if(item.isFieldBlock()) {
            FieldDeclaration fieldDeclaration = item.fieldDeclaration;
        }
        else if(item.isMethodBlock()) {
            FieldDeclaration fieldDeclaration = item.fieldDeclaration;
        }
        else {
            // Block comments without field or method

        }
    }
}
