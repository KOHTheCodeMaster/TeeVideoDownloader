package workspace;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class A1 {

    private static final int THREAD_COUNT = 4;
    static int index1 = 0;
    static List<File> filePartsList;
    private static long sharedCurrentFilePointer;
    private static long startingIndexForEachThread;
    private static int partIndex;

    public static void main(String[] args) {

        A1 obj = new A1();

        try {

            checkArgs();

//        obj.f1();
//        obj.major();
//        f3();
//        download2();

//            filePartsList = new ArrayList<>();
//            for(int i = 1; i <= 5; i++)
//                filePartsList.add(new File("A-" + i + ".part"));
//            download3();
//            merge2(filePartsList);
//            httpRangeReq3();

//        httpRangeReq2();
//        download3();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void checkArgs() {

        String s = "ABC";
        c1(s);
        System.out.println(s);

    }

    private static String c1(String s) {
        s += "...";
        return s;
    }

    private static void merge1() throws IOException, InterruptedException {

        String fName = "A.mp4";
        File targetDir = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\2\\");
        File targetFile = new File(targetDir, fName);
        FileOutputStream fos = new FileOutputStream(targetFile);
        FileChannel fileChannel = fos.getChannel();

//        fileChannel.position(1000);

        long ptr = 0;
        long dx = 5617008;
        for (int i = 1; i <= 8; i++) {

            File file = new File(targetDir, " (" + i + ").part");
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024 * 4];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer);
            }


//            Files.copy(fis, Paths.get(targetFile.getAbsolutePath()));

            /*ReadableByteChannel rbc = Channels.newChannel(fis);
            long bytesRead = fileChannel.transferFrom(rbc, ptr, Long.MAX_VALUE);
            fileChannel.position(bytesRead);
            System.out.println("Bytes Read : " + bytesRead);
            Thread.sleep(1000);
            System.out.println("...");
            ptr += dx;*/
//            fileChannel.position(ptr+ptr+dx);

        }

        System.out.println("DONE!");

    }

/*
    private static void httpRangeReq3() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)  // this is the default
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://http2.github.io/"))
//                .GET()   // this is the default
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Response status code: " + response.statusCode());
        System.out.println("Response headers: " + response.headers());
        System.out.println("Response body: " + response.body());
*/
/*
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    logger.info("Response status code: " + response.statusCode());
                    logger.info("Response headers: " + response.headers());
                    logger.info("Response body: " + response.body());
                });*//*


    }
*/

    private static void download3() {

//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570345647&ei=Tz6ZXZapFoSS1Aa3267oAw&ip=103.82.80.23&id=o-AFp-GZYWTk5ziC1SP4ecS_8_KjyGPOb_aMXbVNSUky6L&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=216250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570323938&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgaFLCiim_jLe2n3H9ADtynx0blpxmzyrs28zAH1JvlZACIQD4-Y5I1aotbMVCTeRDmbkYp3eEF-QM_ma6D9-PBhjJYg%3D%3D&sig=ALgxI2wwRQIhALxZj7ubbnel3634C7uqyEdwANDQtozxBKPBjKXK4h3pAiB_FStEKaYFjt58SpmsfIEfapsc_tUeC5lxEB_QRwHiag==&ratebypass=yes";
//                String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570346550&ei=1kGZXYPZMZrwowPct5PgAQ&ip=103.82.80.23&id=o-AKTuybX9tBorNoVvBOxRVK_lSLZKFH9OSap0ZTOO7uZG&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=165000&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570324839&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhALvTv9Mh0pMKUEiKOXg0DOSKcjnN7n5Q3TIChQQXw4MzAiEA1b5F1dpvb_Y6-qNa7PfU1Jzqu6vTb5GIsyZyA6Z8nOM%3D&sig=ALgxI2wwRgIhAJU1-rHvniuqGBkySiroYNUAZiFztV-vDFNCZg-e0EoAAiEAlDiPuGLn1cmLTE3ACtipU4MvtknBOvM0B9qaM0ZcT-U=&ratebypass=yes";
//        String resourceUrl = " https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570366118&ei=Ro6ZXd-5F5ah3LUPlJu58Ao&ip=103.82.80.23&id=o-AH3-PcOPj2puzX_cUqsLaSMRoF37vIN5o1HqQSwPnmHi&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=325000&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570344427&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIhAI5_PBbtno7BSsZ-_8Lj6Nbzop7IthEZQb9L1pFMOv-_AiAiU2k_hmLQwfP-VEkp2koLAnbd7cyTAE2t9Y5RK_83NA%3D%3D&sig=ALgxI2wwRAIgG7nkMtH5hFgtWgX-s6f2cWLRSC5aIA3OfAs2pKXiEuYCIF5Q4KSxKX5HTq6xtdbcOJsCgknqlZhuXtXmvFEWFmMC&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570368267&ei=qpaZXb3rMNWHz7sPkfyr2Ag&ip=103.82.80.23&id=o-AHJruZfJZoN0__FtrsdHtjFnUYUgf3WlCWVUhhk6rGuC&itag=135&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=255000&mime=video%2Fmp4&gir=yes&clen=22468026&dur=177.677&lmt=1486764624869119&mt=1570346585&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRAIgMY_PgbeJHfvJsM0pCO0-LSHw0w7fIXHbzBl9J8q2xowCIAltRc5mn3IBMEAg2umeAt-uNUHqpDZEdQiY-PIA02-8&sig=ALgxI2wwRgIhANqtektQLjuiC7iYTEiLJ3dyFoNHigVXScJbPP2HzWIjAiEAwxv9grO15EKVbIyfkM55LJJa5FUS5RFRj0Rm9kbyDYc=&ratebypass=yes";
//        String resourceUrl = " https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570970503&ei=J8eiXbeJLJGT3LUPqfio0AY&ip=103.137.85.1&id=o-AFyzE5EOAM3T2KT-0Rr-jbVUUFquMiZrFfhfDMtFFiN4&itag=18&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=563750&mime=video%2Fmp4&gir=yes&clen=14765858&ratebypass=yes&dur=177.771&lmt=1486763357787386&mt=1570948777&fvip=4&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRAIgEfrwfA2ZuwqvccXb_5SJCY1U4z75QLUd7cIJ2MQgUmgCIA8NMHQVEqKtw7AYK1RQGp9SVtURmrQdZOz3XAqVTHKx&sig=ALgxI2wwRAIgTWC6x-eOPR1vgvVD6b8-bQu9CDLeWd5m2ECeP0UHEhMCIFMp4r5OaP1bo39w_OvROO2PRU69yb-aosjhCY-BHzXz"; // Avengers 360P
//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570954647&ei=NomiXeWzI4aUz7sP--GY-Ao&ip=103.137.85.1&id=o-ABol-l0ZUQu0Y_O5fvtvk6Cspmu0R4GwW0TgcKpkz_57&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=182500&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570932935&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRAIgbJVFS8ZvhbR3eZy9Zhsv3ojNFZFOhGOnSdYXI5zGeL4CIGEBsi3GEI1RqQgbMFW_Ikn6EqxMfYoL3aXsrm_3qWeu&sig=ALgxI2wwRQIgV3CjkQaF_u2Rlpz_pycHjICz3a8X4kjYiS-dDXK4LC8CIQDJOSokDR_T29HSmwusfcxUkuoyf0Ap9GL5AzBA7xjK-Q==&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570960295&ei=R5-iXYuRCqaEz7sPqtO4kAI&ip=103.137.85.1&id=o-AG9ZL2-ltIl0FQuu8GUFWyAHY1M6yHSe-V64X-ci_t1n&itag=18&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=955000&mime=video%2Fmp4&gir=yes&clen=14765858&ratebypass=yes&dur=177.771&lmt=1486763357787386&mt=1570938576&fvip=4&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgBV5W32Hd8KpKstjogftju55CnvX3b-FM-lUi3tZ8GKECIQD3D02caAs48dmnKYAtCfxt3M151fhgEkYOcB3NVQQhsg%3D%3D&sig=ALgxI2wwRAIgBpFqekha9mFuMFBH9S_DLy8FlMjodZN3fCOguWtoRVkCIHV8pAV9_ATy-HlKPNmkc-OTF8wyFVWG4y9oLi4P24tQ";
//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570967700&ei=M7yiXbyqPOuFz7sP16K32Ao&ip=103.137.85.1&id=o-AGxaIMMUROjqsUhXlq-o1a7e4wwmjKYqI9V872pVLvB6&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=815000&mime=audio%2Fmp4&gir=yes&clen=2824462&dur=177.771&lmt=1486764369557961&mt=1570946020&fvip=4&keepalive=yes&fexp=23842630&beids=9466585&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAJC52ZBpZqcu5falIRBx2oDqBCUKc_NYolmTEeBTe38XAiEAv4tru1tJpLBuNsRakpbEOvFENZ-Q9T0JIw98VHbl7QQ%3D&sig=ALgxI2wwRgIhAJkzv_tFr2l0v5zka79xWmLXtw0sQnsSlJl6F7HcuiJyAiEA_zYYC0OQ6LUHuRh-h_oRNi1zhlNRqapLXxqVfB7RX1I=&ratebypass=yes";    //  Avengers Audio 1080P
        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570967700&ei=M7yiXbyqPOuFz7sP16K32Ao&ip=103.137.85.1&id=o-AGxaIMMUROjqsUhXlq-o1a7e4wwmjKYqI9V872pVLvB6&itag=299&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=815000&mime=video%2Fmp4&gir=yes&clen=106696268&dur=177.677&lmt=1486764221753451&mt=1570946020&fvip=4&keepalive=yes&fexp=23842630&beids=9466585&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAJC52ZBpZqcu5falIRBx2oDqBCUKc_NYolmTEeBTe38XAiEAv4tru1tJpLBuNsRakpbEOvFENZ-Q9T0JIw98VHbl7QQ%3D&sig=ALgxI2wwRQIgWREr4jt-tUSV-f1gcD6XmSo50TDOiDg0gMEuWnfyi50CIQD2aRLvON93_-VUv3TQzQLu6fvVQ6mHBc23iO_CyyI73Q==&ratebypass=yes";    //  Avengers Video 1080P
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570368528&ei=sJeZXbenDfu7z7sPwNqjgAc&ip=103.82.80.23&id=o-APiecXUhBSdKcWwzzHK1FEii-gtGUP-HVf_i6TB6eTZH&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=358750&mime=audio%2Fmp4&gir=yes&clen=2824462&dur=177.771&lmt=1486764369557961&mt=1570346852&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhANoTIprF3qDPWRKyWCcED_1JInpHO9aHCuBbRZ8LNFM2AiEAygpvdJSk0JkGt8E-grW2krPWMLicsXi2FqwsXgbVBtY%3D&sig=ALgxI2wwRgIhAOWgdmytMEuWtuEBGKTuceFbblPOGSDwXz4KwvsLTJvgAiEAzuA8yEcqTIHXYz7gQvOFVRyKwxi6nZlGcf9UBopoO5Y=&ratebypass=yes";

//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570346550&ei=1kGZXYPZMZrwowPct5PgAQ&ip=103.82.80.23&id=o-AKTuybX9tBorNoVvBOxRVK_lSLZKFH9OSap0ZTOO7uZG&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=165000&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570324839&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhALvTv9Mh0pMKUEiKOXg0DOSKcjnN7n5Q3TIChQQXw4MzAiEA1b5F1dpvb_Y6-qNa7PfU1Jzqu6vTb5GIsyZyA6Z8nOM%3D&sig=ALgxI2wwRgIhAJU1-rHvniuqGBkySiroYNUAZiFztV-vDFNCZg-e0EoAAiEAlDiPuGLn1cmLTE3ACtipU4MvtknBOvM0B9qaM0ZcT-U=&ratebypass=yes";

/*
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\3\\a.mp4");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

        long i1 = System.nanoTime();

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
//            HttpURLConnection urlConnection0 = (HttpURLConnection) urlObj.openConnection();

            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            System.out.println("File Length 1: " + fileLength);
            long dx = fileLength / THREAD_COUNT;   //  Downloaded by Each Thread
            long remainingDataToDownload = fileLength % THREAD_COUNT; //  Download At End

//            InputStream inputStream = urlObj.openStream();
            /*ReadableByteChannel readableByteChannel0 = Channels.newChannel(inputStream);

            FileChannel fileChannel0 = new FileOutputStream(targetFile)
                    .getChannel();*/


            Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
                while (sharedCurrentFilePointer < dx) {
                    System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                    try {
                        Thread.sleep(100);
//                        this.wait(1000);
                        System.out.print("\b\b\b");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("\b\b\b");
                System.out.println("100%\nFile Downloaded Successfully!");

            };

            Runnable downloaderRunnable = () -> {

                long i3 = System.nanoTime();
                try {


                    long j = startingIndexForEachThread;
                    URL urlObj0 = new URL(resourceUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlObj0.openConnection();
                    urlConnection.addRequestProperty("Range", "bytes=" + j + "-" + (j + dx - 1));

                    String fileName = "A-" + ++partIndex + ".part";
                    File tempTargetDir = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\parts\\");
                    File tempTargetFile1 = new File(tempTargetDir, fileName);

                    int rc = urlConnection.getResponseCode();
                    if (rc < 0)
                        System.out.println("\nnum1 : " + rc);
                    else
                        System.out.println("\nnum2 : " + rc);


//                    while ((count = bis.read(data, 0, ONE_MB)) != -1) {
//                        raf.write(data, 0, count);
//                        sharedCurrentFilePointer += count;
//                    }


                    ReadableByteChannel readableByteChannel = Channels.newChannel(urlConnection.getInputStream());
                    FileChannel fileChannel = new FileOutputStream(tempTargetFile1).getChannel();

/*
                    Set<Map.Entry<String, List<String>>> entries = urlConnection.getHeaderFields().entrySet();
                    entries.forEach(e -> {

                        System.out.print(e.getKey() + " : ");
                        e.getValue().forEach(System.out::println);
                        System.out.println("====");

                    });

                    long fileLength2 = Long.parseLong(urlConnection.getHeaderFields().get("Content-Length").get(0));
                    System.out.println("File Length 2: " + fileLength2);
*/
//                    long minorSegment = dx % (ONE_KB * 8);
//                    long majorSegment = dx - minorSegment;
//                    System.out.println(String.format("ms : %s | mj : %s", minorSegment, majorSegment));
                    long tempTotalBytesTransferred = 0;
                    System.out.println("Thread started with FP : " + j);
//                    System.out.println("j + mj : " + (j + majorSegment) + " | ");
//                    long i = j;
//                    for (; i < (j + majorSegment); i += data.length) {
//                        fileChannel.position(i);
                    tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
//                    }
                    System.out.println("temp 1: " + tempTotalBytesTransferred);
//                    tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, j + dx, minorSegment);
//                    System.out.println("temp 2: " + tempTotalBytesTransferred);
                    updateSharedCurrentFilePointer(tempTotalBytesTransferred);
//                    sharedCurrentFilePointer += tempTotalBytesTransferred;

                    filePartsList.add(tempTargetFile1);

                    System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR!!!");
                }
                System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");

            };

//            Thread t = new Thread(downloaderRunnable);
//            t.start();
//            t.join();
            /*
                    2475472 / 8 => 309434
                     309434 / 8 => 38679
             */

            Thread[] threads = new Thread[THREAD_COUNT];
//            Thread displayPercentageThread = new Thread(runnable);
//            displayPercentageThread.start();

            for (int i = 0; i < THREAD_COUNT; i++) {

//                index1++;
//                Thread.sleep(50);
                Thread thread = new Thread(downloaderRunnable);
                thread.start();
                threads[i] = thread;
                Thread.sleep(1500);
//                thread.join();
                startingIndexForEachThread += dx;
//                System.out.println("===");
            }

            for (Thread t : threads) {
                t.join();
                System.out.println("f : " + sharedCurrentFilePointer);
            }

//            startingIndexForEachThread -= dx;

            if (remainingDataToDownload != 0)
                downloadLeftover(resourceUrl, remainingDataToDownload, startingIndexForEachThread);

            merge2(filePartsList);


            Thread.sleep(20);
//            displayPercentageThread.join();
            System.out.println("fp : " + sharedCurrentFilePointer);
            sharedCurrentFilePointer = 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);


    }

    private static void downloadLeftover(String resourceUrl, long remainingDataToDownload, long startingIndexForEachThread) {

        /*
            orig :      14765858
            dx =         3691464        7382928         11074392            14765856
            downloaded   0-3691463  3691464-7382927    7382928-11074391  11074392-14765855
            remaining :

         */
        long i3 = System.nanoTime();
        try {

            long j = A1.startingIndexForEachThread;
            System.out.println("jjjj == >  " + j);
            URL urlObj0 = new URL(resourceUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj0.openConnection();
            urlConnection.addRequestProperty("Range", "bytes=" + j + "-");// /*j + */"-" + ( remainingDataToDownload));

            String fileName = "A-" + ++partIndex + ".part";
            File tempTargetDir = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\parts\\");
            File tempTargetFile1 = new File(tempTargetDir, fileName);

            int rc = urlConnection.getResponseCode();
            if (rc < 0)
                System.out.println("\nnum1 : " + rc);
            else
                System.out.println("\nnum2 : " + rc);

            ReadableByteChannel readableByteChannel = Channels.newChannel(urlConnection.getInputStream());
            FileChannel fileChannel = new FileOutputStream(tempTargetFile1).getChannel();

            long tempTotalBytesTransferred = 0;
            System.out.println("Thread started with FP : " + j);

            tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            System.out.println("temp 1: " + tempTotalBytesTransferred);

            updateSharedCurrentFilePointer(tempTotalBytesTransferred);
            filePartsList.add(tempTargetFile1);

            System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR!!!");
        }
        System.out.println("time taken : " + (System.nanoTime() - i3) / 1E9 + " sec.");


    }

    /*

    Orig. : 1080P Avengers - Video Only
    url = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570967700&ei=M7yiXbyqPOuFz7sP16K32Ao&ip=103.137.85.1&id=o-AGxaIMMUROjqsUhXlq-o1a7e4wwmjKYqI9V872pVLvB6&itag=299&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=815000&mime=video%2Fmp4&gir=yes&clen=106696268&dur=177.677&lmt=1486764221753451&mt=1570946020&fvip=4&keepalive=yes&fexp=23842630&beids=9466585&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAJC52ZBpZqcu5falIRBx2oDqBCUKc_NYolmTEeBTe38XAiEAv4tru1tJpLBuNsRakpbEOvFENZ-Q9T0JIw98VHbl7QQ%3D&sig=ALgxI2wwRQIgWREr4jt-tUSV-f1gcD6XmSo50TDOiDg0gMEuWnfyi50CIQD2aRLvON93_-VUv3TQzQLu6fvVQ6mHBc23iO_CyyI73Q==&ratebypass=yes"
    File Length 1: 106696268
    Download Time : 297.133310699

    Orig. : 1080P Avengers - Audio Only
    url = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570967700&ei=M7yiXbyqPOuFz7sP16K32Ao&ip=103.137.85.1&id=o-AGxaIMMUROjqsUhXlq-o1a7e4wwmjKYqI9V872pVLvB6&itag=140&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=815000&mime=audio%2Fmp4&gir=yes&clen=2824462&dur=177.771&lmt=1486764369557961&mt=1570946020&fvip=4&keepalive=yes&fexp=23842630&beids=9466585&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAJC52ZBpZqcu5falIRBx2oDqBCUKc_NYolmTEeBTe38XAiEAv4tru1tJpLBuNsRakpbEOvFENZ-Q9T0JIw98VHbl7QQ%3D&sig=ALgxI2wwRgIhAJkzv_tFr2l0v5zka79xWmLXtw0sQnsSlJl6F7HcuiJyAiEA_zYYC0OQ6LUHuRh-h_oRNi1zhlNRqapLXxqVfB7RX1I=&ratebypass=yes"
    File Length 1: 2824462
    Download Time : 73.3303273

    Orig. : 360P Avengers
    File Length 1: 14765858
    Download Time : 43.3360472

    Parts : 360P Avengers
    Target Video File Size : 14765858
    Merging Time : 0.027268099 seconds
    Download Time : 44.4370809

    Parts : 1080P Avengers
    Target Video File Size : 106696268
    Merging Time : 0.1060606 seconds
    Download Time : 300.027810799
     */

    private static void merge2(List<File> filesList) {

        long i1 = System.nanoTime();
        try {
            File tempTargetDir = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\parts\\");

            String targetFileName = "Final-Video.mp4";
            File targetVideoFile = new File(tempTargetDir, targetFileName);
            long pos = 0;

            for (int i = 1; i <= filesList.size(); i++) {

                String fName = "A-" + i + ".part";
                File f = new File(tempTargetDir, fName);
                System.out.println("f : " + f.getName());
                System.out.println("pos : " + pos);
                FileInputStream fis = new FileInputStream(f);
                ReadableByteChannel rbc = Channels.newChannel(fis);

                FileOutputStream fos = new FileOutputStream(targetVideoFile, true);
                FileChannel fc = fos.getChannel();

                fc.position(pos * pos);
                fc.transferFrom(rbc, pos, Long.MAX_VALUE);
                pos += f.length();

            }

            System.out.println("Target Video File Size : " + targetVideoFile.length());

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println((System.nanoTime() - i1) / 1E9 + " seconds");

    }

    private static void httpRangeReq3() {

//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570293659&ei=O3OYXYesC_SMmge3-rPoDg&ip=103.82.80.26&id=o-AMoPD5QIxj_q1ynITesM2EylpPzuCz3MlgYU3fP6h58W&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=351250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570271937&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhANS8E--fhAAt_8dA8FTu-oYg8TSp0qYtWR9V1MLRvrVpAiEA_l6DRVUQ0frOeFh_YdXAGa8NxqTaNF5lUsKY8ZCt9ro%3D&sig=ALgxI2wwRQIgYTvu-wcZxd2r7UuyBbCi74nrMvYRP3yUo7zH2ibmvjgCIQD9xpUcYGlQKF8wsBZVmOg8y-o0frec9cW8ntPCpW4ViA==&ratebypass=yes";
//        String resourceUrl = "https://www.youtube.com/watch?v=fPrixQcSPyM";
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570345647&ei=Tz6ZXZapFoSS1Aa3267oAw&ip=103.82.80.23&id=o-AFp-GZYWTk5ziC1SP4ecS_8_KjyGPOb_aMXbVNSUky6L&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=216250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570323938&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgaFLCiim_jLe2n3H9ADtynx0blpxmzyrs28zAH1JvlZACIQD4-Y5I1aotbMVCTeRDmbkYp3eEF-QM_ma6D9-PBhjJYg%3D%3D&sig=ALgxI2wwRQIhALxZj7ubbnel3634C7uqyEdwANDQtozxBKPBjKXK4h3pAiB_FStEKaYFjt58SpmsfIEfapsc_tUeC5lxEB_QRwHiag==&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570987698&ei=UgqjXZiiE_ie3LUPnYC4sAo&ip=103.137.85.1&id=o-AHL5D9J17pLaQfxp7AIQbpVWgPVuslSUtBadQDob2WvS&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=201250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570966010&fvip=4&keepalive=yes&fexp=23842630&beids=9466588&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRAIgBqFJG-6ajfjsESOqnMIHaY1WogJwotHeQriq01C7q2MCIApS_nWmFnsRIi8S-piwex7VcvhPosYAx5uknWQDMzze&sig=ALgxI2wwRgIhAPooVCVYnj-AHgS5-J1o64pRuhR2EOqmu-BUw7e3avlDAiEA-b8TTsVy4Fvr6kv840zB0KruxQl80Ize5DhEhyJGtA4=&ratebypass=yes";
        String resourceUrl = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Want-Digest";
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\a.txt");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long i1 = System.nanoTime();
        int rc;

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            long startPos = 0, endPos = 999;
//            urlConnection.addRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
//                  Want-Digest: MD5;q=0.3, sha;q=1
//            urlConnection.addRequestProperty("Want-Digest", "");
            urlConnection.addRequestProperty("Want-Digest", "SHA-512;q=0.3, sha-256;q=1, md5;q=0");

            rc = urlConnection.getResponseCode();
            if (rc < 0)
                System.out.println("\nnum1 : " + rc);
            else
                System.out.println("\nnum2 : " + rc);

//            urlConnection.connect();
//            urlConnection.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
//            System.out.println("\nnum : " + urlConnection.getResponseCode());
//            urlConnection.getRequestMethod();
            Set<Map.Entry<String, List<String>>> entries = urlConnection.getHeaderFields().entrySet();
            System.out.println(urlConnection.getResponseMessage());

            entries.forEach(e -> {

                System.out.print(e.getKey() + " : ");
//                System.out.print("Value List : ");
                e.getValue().forEach(System.out::println);
                System.out.println("====");

            });


            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            long dx = fileLength / 8;   //  Downloaded by Each Thread
            long remainingAshData = fileLength % 8; //  Download At End


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);


    }

    private static void httpRangeReq2() {

//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
//        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570293659&ei=O3OYXYesC_SMmge3-rPoDg&ip=103.82.80.26&id=o-AMoPD5QIxj_q1ynITesM2EylpPzuCz3MlgYU3fP6h58W&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=351250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570271937&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhANS8E--fhAAt_8dA8FTu-oYg8TSp0qYtWR9V1MLRvrVpAiEA_l6DRVUQ0frOeFh_YdXAGa8NxqTaNF5lUsKY8ZCt9ro%3D&sig=ALgxI2wwRQIgYTvu-wcZxd2r7UuyBbCi74nrMvYRP3yUo7zH2ibmvjgCIQD9xpUcYGlQKF8wsBZVmOg8y-o0frec9cW8ntPCpW4ViA==&ratebypass=yes";
//        String resourceUrl = "https://www.youtube.com/watch?v=fPrixQcSPyM";
        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570345647&ei=Tz6ZXZapFoSS1Aa3267oAw&ip=103.82.80.23&id=o-AFp-GZYWTk5ziC1SP4ecS_8_KjyGPOb_aMXbVNSUky6L&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=216250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570323938&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRQIgaFLCiim_jLe2n3H9ADtynx0blpxmzyrs28zAH1JvlZACIQD4-Y5I1aotbMVCTeRDmbkYp3eEF-QM_ma6D9-PBhjJYg%3D%3D&sig=ALgxI2wwRQIhALxZj7ubbnel3634C7uqyEdwANDQtozxBKPBjKXK4h3pAiB_FStEKaYFjt58SpmsfIEfapsc_tUeC5lxEB_QRwHiag==&ratebypass=yes";
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\a.txt");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long i1 = System.nanoTime();

        int ONE_MB = (int) 1E6 * 2;
        int ONE_KB = 1024;
        byte[] buffer = new byte[ONE_MB];
        int rc;

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();

            long startPos = 0, endPos = 999;
            urlConnection.addRequestProperty("Range", "bytes=" + startPos + "-" + endPos);

            rc = urlConnection.getResponseCode();
            if (rc < 0)
                System.out.println("\nnum1 : " + rc);
            else
                System.out.println("\nnum2 : " + rc);

//            urlConnection.connect();
//            urlConnection.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
//            System.out.println("\nnum : " + urlConnection.getResponseCode());
//            urlConnection.getRequestMethod();
            Set<Map.Entry<String, List<String>>> entries = urlConnection.getHeaderFields().entrySet();

            entries.forEach(e -> {

                System.out.print(e.getKey() + " : ");
//                System.out.print("Value List : ");
                e.getValue().forEach(System.out::println);
                System.out.println("====");

            });


            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            long dx = fileLength / 8;   //  Downloaded by Each Thread
            long remainingAshData = fileLength % 8; //  Download At End

            InputStream inputStream = urlObj.openStream();
            ReadableByteChannel readableByteChannel0 = Channels.newChannel(inputStream);

            FileChannel fileChannel0 = new FileOutputStream(targetFile)
                    .getChannel();

            System.out.println("File Length 1: " + fileLength);
            Thread.sleep(1000);
            /*
                    2475472 / 8 => 309434
                     309434 / 8 => 38679
             */
            urlConnection = null;
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.addRequestProperty("Range", "bytes=" + 2000 + "-");

            rc = urlConnection.getResponseCode();
            if (rc < 0)
                System.out.println("\nnum1 : " + rc);
            else
                System.out.println("\nnum2 : " + rc);


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);


    }

    private static void httpRangeReq1() {

//        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
        String resourceUrl = "https://r1---sn-g5pauxapo-qxae.googlevideo.com/videoplayback?expire=1570293659&ei=O3OYXYesC_SMmge3-rPoDg&ip=103.82.80.26&id=o-AMoPD5QIxj_q1ynITesM2EylpPzuCz3MlgYU3fP6h58W&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-g5pauxapo-qxae%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=351250&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570271937&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhANS8E--fhAAt_8dA8FTu-oYg8TSp0qYtWR9V1MLRvrVpAiEA_l6DRVUQ0frOeFh_YdXAGa8NxqTaNF5lUsKY8ZCt9ro%3D&sig=ALgxI2wwRQIgYTvu-wcZxd2r7UuyBbCi74nrMvYRP3yUo7zH2ibmvjgCIQD9xpUcYGlQKF8wsBZVmOg8y-o0frec9cW8ntPCpW4ViA==&ratebypass=yes";
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\a.txt");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long i1 = System.nanoTime();

        int ONE_MB = (int) 1E6 * 2;
        int ONE_KB = 1024;
        byte[] buffer = new byte[ONE_MB];

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
            URLConnection urlConnection = urlObj.openConnection();

            Set<Map.Entry<String, List<String>>> entries = urlConnection.getHeaderFields().entrySet();

            entries.forEach(e -> {

                System.out.println("Key : " + e.getKey());
                System.out.println("Value List : ");
                e.getValue().forEach(System.out::println);
                System.out.println("====");

            });


            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            long dx = fileLength / 8;   //  Downloaded by Each Thread
            long remainingAshData = fileLength % 8; //  Download At End

            InputStream inputStream = urlObj.openStream();
            ReadableByteChannel readableByteChannel0 = Channels.newChannel(inputStream);

            FileChannel fileChannel0 = new FileOutputStream(targetFile)
                    .getChannel();

            System.out.println("File Length 1: " + fileLength);
            Thread.sleep(1000);
            /*
                    2475472 / 8 => 309434
                     309434 / 8 => 38679
             */


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);


    }

    private static void download1() {

        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\a.mp4");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long i1 = System.nanoTime();

        int ONE_MB = (int) 1E6 * 2;
        int ONE_KB = (int) 1024;
        byte[] buffer = new byte[ONE_MB];

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            long dx = (long) (fileLength * .1);
            InputStream inputStream = urlObj.openStream();
            ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
            FileChannel fileChannel = new FileOutputStream(targetFile)
                    .getChannel();

/*
            for(String s : urlObj.openConnection().getHeaderFields().keySet())
                System.out.println(s);
            List<String> l1 = urlObj.openConnection().getHeaderFields().get("Content-Length");
            for(String s : l1)
                System.out.println(s);
*/
            System.out.println("File Length 1: " + fileLength);

            Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
                while (sharedCurrentFilePointer < dx) {
                    System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                    try {
                        Thread.sleep(100);
//                        this.wait(1000);
                        System.out.print("\b\b\b");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("\b\b\b");
                System.out.println("100%\nFile Downloaded Successfully!");

            };

            Runnable downloaderRunnable = () -> {

//                System.out.println("thread started : " + System.nanoTime()/ 1E9);
                try {

                    byte[] data = new byte[ONE_KB * 8];
//                    while ((count = bis.read(data, 0, ONE_MB)) != -1) {
//                        raf.write(data, 0, count);
//                        sharedCurrentFilePointer += count;
//                    }
                    long j = startingIndexForEachThread;
                    long f = 0;
                    System.out.println("Thread started with FP : " + startingIndexForEachThread);
                    for (long i = j; i < j + dx; i += data.length) {
                        long bytesTransferred = fileChannel.transferFrom(readableByteChannel, i, data.length);
                        f += bytesTransferred;
                    }

                    updateSharedCurrentFilePointer(f);
//                    sharedCurrentFilePointer += f;
                    System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            };

            Thread[] threads = new Thread[10];

            Thread displayPercentageThread = new Thread(runnable);
            displayPercentageThread.start();
//            Thread downloaderThread = new Thread(downloaderRunnable);
//            downloaderThread.start();
//
//            threads[0] = downloaderThread;

            for (int i = 0; i < 10; i++) {

                Thread.sleep(3000);
                Thread thread = new Thread(downloaderRunnable);
                thread.start();
                threads[i] = thread;
                Thread.sleep(3000);
                startingIndexForEachThread += dx;
                System.out.println("===");
            }

            for (Thread t : threads) {
                t.join();
                System.out.println("f : " + sharedCurrentFilePointer);
            }
/*
            //  DAMAGE Entire File!
            for (long i = 0; i < fileLength; i += buffer.length) {
                fileChannel.transferFrom(readableByteChannel, i, buffer.length);
                sharedCurrentFilePointer = i;
            }
*/

            Thread.sleep(20);
//            sharedCurrentFilePointer += buffer.length * 2;
            displayPercentageThread.join();
//            downloaderThread.join();
            System.out.println("fp : " + sharedCurrentFilePointer);
            sharedCurrentFilePointer = 0;

//            d1(fileLength);

//            fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);

    }

    private static void download2() {

        String resourceUrl = "https://r1---sn-pni5ooxunva-cvhe.googlevideo.com/videoplayback?expire=1570025307&ei=-1qUXbmoCYLioQOIhZvACQ&ip=103.137.85.1&id=o-ABMQrRjTsXgRiqnOr7u_4UPL7ZRzVC-8DAHW46CsPDEc&itag=160&aitags=133%2C134%2C135%2C136%2C137%2C160%2C242%2C243%2C244%2C247%2C248%2C271%2C278%2C298%2C299%2C302%2C303%2C308%2C313%2C315&source=youtube&requiressl=yes&mm=31%2C29&mn=sn-pni5ooxunva-cvhe%2Csn-qxa7sn7r&ms=au%2Crdu&mv=m&mvi=0&pl=24&initcwndbps=718750&mime=video%2Fmp4&gir=yes&clen=2475472&dur=177.677&lmt=1486764857043883&mt=1570003608&fvip=4&keepalive=yes&fexp=23842630&c=WEB&sparams=expire%2Cei%2Cip%2Cid%2Caitags%2Csource%2Crequiressl%2Cmime%2Cgir%2Cclen%2Cdur%2Clmt&lsparams=mm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AHylml4wRgIhAN5qiTJbsyg5oDyLNtXBiY-mGyYO7_BnNq6gjFc8oJR5AiEA2yUJOQslK4-SgkamM1MyQVmU__9guN-ildox5Y38ra0%3D&sig=ALgxI2wwRgIhAIirfdQrwh6I_xMT-4vtNb_q4aiGho8DnJGd9twBO-h2AiEAq3P9WWUR25Kfft0bOWL1yuCexdnZR4igtH_c-Mp9kjQ=&ratebypass=yes";
        File targetFile = new File("F:\\CODE-ZONE\\JAVA Codes\\IntellijProjects\\Network\\VideoDownloader\\TeeVideoDownloader\\res\\downloaded\\2\\a\\a.mp4");
        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long i1 = System.nanoTime();

        int ONE_MB = (int) 1E6 * 2;
        int ONE_KB = 1024;
        byte[] buffer = new byte[ONE_MB];

        System.out.println("URL : " + resourceUrl + "\n\n");

        try {

            URL urlObj = new URL(resourceUrl);
            long fileLength = Long.parseLong(urlObj.openConnection().getHeaderFields().get("Content-Length").get(0));
            long dx = fileLength / 8;   //  Downloaded by Each Thread
            long remainingAshData = fileLength % 8; //  Download At End

            InputStream inputStream = urlObj.openStream();
            ReadableByteChannel readableByteChannel0 = Channels.newChannel(inputStream);

            FileChannel fileChannel0 = new FileOutputStream(targetFile)
                    .getChannel();

            System.out.println("File Length 1: " + fileLength);

            Runnable runnable = () -> {
            /*
                Time Stamp : 22nd August 2K19, 12:56 AM..!!
                sharedCurrentFilePointer -> value of i i.e. current Pos.
                        Following Condition :
                (sharedCurrentFilePointer + buffer.length > fileLength) == true
                only when the Main Thread has completed the Processing.
             */
                while (sharedCurrentFilePointer < dx) {
                    System.out.print((sharedCurrentFilePointer * 100 / fileLength) + "%");

                    try {
                        Thread.sleep(100);
//                        this.wait(1000);
                        System.out.print("\b\b\b");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("\b\b\b");
                System.out.println("100%\nFile Downloaded Successfully!");

            };

            Runnable downloaderRunnable = () -> {

//                System.out.println("thread started : " + System.nanoTime()/ 1E9);
                try {

                    byte[] data = new byte[ONE_KB * 8];
//                    while ((count = bis.read(data, 0, ONE_MB)) != -1) {
//                        raf.write(data, 0, count);
//                        sharedCurrentFilePointer += count;
//                    }
                    FileChannel fileChannel = fileChannel0;
                    ReadableByteChannel readableByteChannel = readableByteChannel0;
                    long j = startingIndexForEachThread;
                    long minorSegment = dx % (ONE_KB * 8);
                    long majorSegment = dx - minorSegment;
//                    System.out.println(String.format("ms : %s | mj : %s", minorSegment, majorSegment));
                    long tempTotalBytesTransferred = 0;
                    System.out.println("Thread started with FP : " + j);
                    System.out.println("j + mj : " + (j + majorSegment) + " | ");
                    long i = j;
                    for (; i < (j + majorSegment); i += data.length) {
//                        fileChannel.position(i);
                        tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, i, data.length);
                    }
                    System.out.println("temp 1: " + tempTotalBytesTransferred);
                    tempTotalBytesTransferred += fileChannel.transferFrom(readableByteChannel, i, minorSegment);
                    System.out.println("temp 2: " + tempTotalBytesTransferred);
                    updateSharedCurrentFilePointer(tempTotalBytesTransferred);
//                    sharedCurrentFilePointer += tempTotalBytesTransferred;
                    System.out.println("Thread Completed | fp : " + sharedCurrentFilePointer);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR!!!");
                }

            };

            /*
                    2475472 / 8 => 309434
                     309434 / 8 => 38679
             */

            Thread[] threads = new Thread[8];
//            Thread displayPercentageThread = new Thread(runnable);
//            displayPercentageThread.start();

            for (int i = 0; i < 8; i++) {

//                Thread.sleep(3000);
                Thread thread = new Thread(downloaderRunnable);
                thread.start();
                threads[i] = thread;
                Thread.sleep(3000);
//                thread.join();
                startingIndexForEachThread += dx;
                System.out.println("===");
            }

            for (Thread t : threads) {
                t.join();
                System.out.println("f : " + sharedCurrentFilePointer);
            }

            Thread.sleep(20);
//            displayPercentageThread.join();
            System.out.println("fp : " + sharedCurrentFilePointer);
            sharedCurrentFilePointer = 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDone!");

        long i2 = System.nanoTime();
        System.out.println("\n\nDownload Time : " + (i2 - i1) / 1E9);


    }

    private static synchronized void updateSharedCurrentFilePointer(long f) {
        sharedCurrentFilePointer += f;
    }

    private static void f3() {

        String s1 = "a1\nbc";
        String s2 = s1.replaceAll("\\n", "-");
        System.out.println(s2);

    }

    private void major() {

        try {

            System.out.println("Enter Src Dir.: ");
            String srcPath = new Scanner(System.in).nextLine();
            MyFileVisitor myFileVisitor = new MyFileVisitor();
            Files.walkFileTree(Paths.get(srcPath), myFileVisitor);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void f1() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Path: ");
        String fN = scanner.nextLine();
        File file = new File(fN);
//        "E:/ZZZ123/Zz1VP9/30 sec 1080P 30FPS VP9 WEBM.webm"

        f2(file);

    }

    private void f2(File file) {

        String ext = ".mp4";
        int dotPos = file.getName().lastIndexOf(".");
        String newFN = file.getParent() + "/" + file.getName().substring(0, dotPos) + ext;
        System.out.println("New File Name : " + newFN);
        boolean b = file.renameTo(new File(newFN));
        if (b)
            System.out.println("Renamed Successfully! : " + file.getAbsolutePath());
        else
            System.out.println("Failed!!!");

    }

    public void logBuildOptions(StringBuilder stringBuilder) {

        int i = 0;
        System.out.println(i + ". builder:");
        System.out.println(stringBuilder);
        System.out.println();

    }

    class MyFileVisitor implements FileVisitor<Path> {

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            f2(file.toFile());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }
    }


}
