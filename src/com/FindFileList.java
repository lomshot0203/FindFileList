package com;

import java.io.*;
import java.util.*;

public class FindFileList {

    private static HashMap<String, String> sysPathList = new HashMap<>();
    private static int fileCnt = 0;

    public static void main(String[] args) {
//        System.out.println("asd");
//        getSysFileList(args[0], args[1]);
        getSysFileList("D:\\allSharpFnc\\workspace\\AllSharpPlus\\WebContent\\", "", "", "jasper");
//        getSysProps();
//        getSystemEnv();
    }

    /**
     * String root (루트)
     * String fileNm (파일명)
     * String extesion (확장자)
     * String content (내용)
     * */
    private static void getSysFileList(String root, String fileNm, String extesion, String content) {
        ArrayList<File> rootList = new ArrayList<>();
        ArrayList<String> accDeniedPathList = new ArrayList<>();

        if (root == "") {
            rootList = getRootDirList(root);
        } else {
            rootList.add(new File(root));
        }

        for (File file : rootList) {
            File[] ll = file.listFiles();
            for (File f : ll) {
                int rst = printAllFiles(f.listFiles(), fileNm, extesion, content, f.getAbsolutePath().toString());
                if (rst == -1) {
                    accDeniedPathList.add(f.getAbsolutePath().toString());
                    continue;
                }
            }
        }
        if (sysPathList.size() == 0) {
            System.out.println("파일없음");
        } else {
            writeFileList();
            System.out.println("찾기완료");
        }
    }

    private static int printAllFiles (File[] list, String fileNm, String extension, String content, String accDeniedPath){
        if (list == null ) {
            return -1;
        }
        System.out.println(fileCnt++);
        for (File file : list) {
            if (file != null && file.exists() && file.canRead()) {
                if (file.isFile()) {
                    String path = file.getAbsolutePath();
                    if (fileNm != "") {
                        findFileName(path, content);
                    }

                    if (extension != "") {
                        findFileExtension(path, extension);
                    }

                    if (content != "") {
                        findFileContent(path, content);
                    }

                    if (fileNm == "" && extension == "" && content == "") {
                        sysPathList.put(path, path);
                    }
                }

                if (file.isDirectory()) {
                    printAllFiles(file.listFiles(), fileNm, extension, content, accDeniedPath);
                }
            }
        }
        return 1;
    }

    /*파일이름으로 찾기*/
    private static void findFileName(String path, String FileNm) {
        if (path.contains(FileNm)) {
            sysPathList.put(path, path);
        }
    }

    /*파일 확장자로 찾기*/
    private static void findFileExtension(String path, String extention) {
        if (path.endsWith(extention)) {
            sysPathList.put(path, path);
        }
    }

    /*파일 내용으로 찾기*/
    private static void findFileContent (String path, String content) {
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.contains(content)) {
                    sysPathList.put(path.substring(path.lastIndexOf('\\')+1), path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 찾은 파일리스트 출력*/
    private static void writeFileList () {
        File file = new File("C:\\Users\\wonyoung\\Desktop\\fileList.txt");
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
            Iterator<String> it = sysPathList.keySet().iterator();
            while (it.hasNext()) {
                bf.write(it.next());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getSystemEnv() { /*환경변수 조회*/
        System.out.println("=========================== System.getenv ============================");
        Map<String, String> map =  System.getenv();
        Set set =  map.keySet();
        for (Iterator<Set> it = set.iterator(); it.hasNext();) {
            System.out.println(it.next() +" : "+ map.get(it.next()));
        }
    }

    private static void getSysProps () { /*시스템 사양, 자바버전등 조회*/
        System.out.println("=========================== System.getProperties ============================");
        Properties props = System.getProperties();
        Set<Map.Entry<Object, Object>> set = props.entrySet();
        Iterator<Map.Entry<Object, Object>> it= set.iterator();
        while (it.hasNext()) {
            Map.Entry<Object, Object> map = it.next();
            System.out.println("key : "+ map.getKey());
            String value = map.getValue().toString();
            System.out.println("value : "+ (value.length() > 32 ? value.substring(0, 32)+"..." : value ) );
        }
    }

    /* 루트 디렉토리 조회*/
    private static ArrayList<File> getRootDirList (String str) {
        System.out.println("=========================== File.listRoots() ============================");
        ArrayList<File> list = new ArrayList<>();
        File[] roots = File.listRoots();
        for (File root : roots) {
            if (root.getAbsolutePath().startsWith(str)){
                list.add(root);
            }
        }
        return list;
    }
}
