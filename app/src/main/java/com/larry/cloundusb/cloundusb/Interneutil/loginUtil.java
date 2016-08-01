package com.larry.cloundusb.cloundusb.Interneutil;

import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

import com.larry.cloundusb.cloundusb.activity.MainActivity;
import com.larry.cloundusb.cloundusb.activity.PersonalSettingActivity;
import com.larry.cloundusb.cloundusb.activity.RegisterActivity;
import com.larry.cloundusb.cloundusb.application.GetContextUtil;
import com.larry.cloundusb.cloundusb.config.config;
import com.larry.cloundusb.cloundusb.constant.InternetConfig;
import com.larry.cloundusb.cloundusb.fileutil.FileBox;
import com.larry.cloundusb.cloundusb.jsonutil.JsonUtil;
import com.larry.cloundusb.cloundusb.util.EncipherUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Larry on 6/5/2016.
 */
public class loginUtil {


    final static String TAG = "loginutil";
    public static int resultCode;
    public static int Percent;

    /*用户登录请求
    *
    *
    * return
    * -2表示用户不存在
    *
    * 1表示成功
    *
    * -1表示登录密码错误
    *
    *
    * */
    static public void loginRequest(final String name, final String password) {
        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String md5password = EncipherUtil.getMD5(password);
                    OkHttpUtils
                            .get()
                            .url(InternetConfig.login_interface)
                            .addParams("act", "login")
                            .addParams("username", name)
                            .addParams("password", md5password)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    resultCode = -1;
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e(TAG, response);
                                    try {
                                        JSONObject ob = new JSONObject(response);
                                        if (ob.getString("is_ok").equals("-2")) {
                                            RegisterActivity.handler.sendEmptyMessage(-2);
                                            Looper.loop();
                                        } else if (ob.getString("is_ok").equals("1")) {

                                            MainActivity.user.setUserId(ob.getString("user_id"));
                                            MainActivity.user.setPortraitId(Integer.valueOf(ob.getString("head_pic")));
                                            MainActivity.user.setPetname(ob.getString("nick_name"));
                                            MainActivity.user.setLogin(true);
                                            MainActivity.updateLoginInfor(MainActivity.user);
                                            loginUtil.getusbFile();
                                            RegisterActivity.handler.sendEmptyMessage(1);
                                        } else if (ob.getString("is_ok").equals("-1")) {
                                            RegisterActivity.handler.sendEmptyMessage(-1);
                                            Looper.loop();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "异常" + e.getCause());
                                    }


                                }
                            });

                } catch (NoSuchAlgorithmException e) {

                }
            }
        });

    }

    /*
    *
    * 登录util
    *
    * */
    static public void sendFile(final String filePath,final String fileName) {

        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);


                boolean fileexits = file.exists();
                OkHttpUtils.post()
                        .addFile("test1", fileName, file)
                        .url(InternetConfig.upload_file_interface)
                        .addParams("act", "load")
                        .addParams("user_id", MainActivity.user.getUserId())
                        .build()
                        .execute(new Callback() {
                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                                Percent=(int)(progress*100);
                            }

                            @Override
                            public Object parseNetworkResponse(Response response, int id) throws Exception {
                                return null;
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(Object response, int id) {

                            }
                        });


            }
        });

    }


    /*
    *
    * 反馈函数，反馈意见到后台
    *  1 成功
    *
    *  -1失败
    *
    * */
    static public void feedbackRequest(final String userID, final String content, final String emailAddress) {

        MainActivity.sexecutorService.submit(new Runnable() {
            int responseCode;//返回的码

            @Override
            public void run() {

                String url = InternetConfig.feedback_interface;
                OkHttpUtils
                        .get()
                        .url(url)
                        .addParams("username", userID)
                        .addParams("content", content)
                        .addParams("connection_way", emailAddress)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {
                                responseCode = -1;

                            }

                            @Override
                            public void onResponse(String response, int id) {

                                try {
                                    Log.e(TAG, response);
                                    JSONObject ob = new JSONObject(response);
                                    String responseCode = ob.getString("is_ok");
                                    Log.e(TAG, responseCode);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });


    }


    /*
    * 扫描登录接口
    *
    * */

    static public int scanLogin(final String userId, final String detailInfo) {

        MainActivity.sexecutorService.submit(new Runnable() {
            int responseCode;//返回的码

            @Override
            public void run() {

                Log.e(TAG + "id", userId);
                OkHttpUtils
                        .get()
                        .url(InternetConfig.login_interface)
                        .addParams("act", "qr_login")
                        .addParams("user_name", "")
                        .addParams("password", "")
                        .addParams("detail_info", detailInfo)
                        .addParams("user_id", userId)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(okhttp3.Call call, Exception e, int id) {
                                responseCode = -1;
                                Log.e(TAG, "错误" + e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    Log.e(TAG, response);
                                    JSONObject ob = new JSONObject(response);
                                    if (ob.getString("is_ok").compareTo("1") != 0) ;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "异常dasd" + e.getCause());
                                }


                            }
                        });


            }
        });
        return 1;


    }



    /*
    * 查看U盘接口
    *
    * */





    /*
    *
    * 注册接口
    *
    * */

    static public int register(final String username, final String password) {


        MainActivity.sexecutorService.submit(new Runnable() {
            int responseCode;//返回的码

            @Override
            public void run() {

                try {
                    OkHttpUtils
                            .get()
                            .url(InternetConfig.login_interface)
                            .addParams("act", "register")
                            .addParams("username", username)
                            .addParams("password", EncipherUtil.getMD5(password))
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(okhttp3.Call call, Exception e, int id) {
                                    responseCode = -1;
                                    Log.e(TAG, "错误" + e);
                                }

                                @Override
                                public void onResponse(String response, int id) {

                                    Log.e(TAG, response);
                                    try {
                                        Log.e(TAG, response);
                                        JSONObject ob = new JSONObject(response);
                                        if (ob.getString("is_ok").compareTo("1") != 0) {
                                            MainActivity.user.setUserId(ob.getString("user_id"));
                                            MainActivity.user.setLogin(true);

                                            RegisterActivity.handler.sendEmptyMessage(1);
                                            Looper.loop();

                                        } else if (ob.getString("is_ok").compareTo("-1") != 0) {

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e(TAG, "异常dasd");
                                    }
                                }
                            });

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        return 1;
    }

    /*
    * 获取u盘文件列表
    * */
    static public void getusbFile() {

        MainActivity.sexecutorService.submit(new Runnable() {

            @Override
            public void run() {
                Log.e(TAG, "静茹额线程");
                Log.e(TAG, MainActivity.user.getUserId());
                OkHttpUtils
                        .get()
                        .url(InternetConfig.upload_file_interface)
                        .addParams("act", "download_list")
                        .addParams("user_id", MainActivity.user.getUserId())
                        .build()
                        .execute(new StringCallback() {

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Log.e(TAG, "errorrrr" + e.getMessage() + "   " + e.getCause());

                            }


                            @Override
                            public void onResponse(String response, int id) {
                                try {

                                    String content = new JSONObject(response).getString("file_list");
                                    JSONArray jsonArrayOthers = new JSONArray(new JSONObject(content).getString("others"));
                                    JSONArray jsonArrayImages = new JSONArray(new JSONObject(content).getString("images"));
                                    JSONArray jsonArrayMusices = new JSONArray(new JSONObject(content).getString("musics"));
                                    JSONArray jsonArrayVideos = new JSONArray(new JSONObject(content).getString("videos"));
                                    JSONArray jsonArrayDocuments = new JSONArray(new JSONObject(content).getString("documents"));
                                    List<JSONArray> jsonArraysList = new ArrayList<JSONArray>();
                                    jsonArraysList.add(jsonArrayImages);
                                    jsonArraysList.add(jsonArrayMusices);
                                    jsonArraysList.add(jsonArrayVideos);
                                    jsonArraysList.add(jsonArrayDocuments);
                                    jsonArraysList.add(jsonArrayOthers);
                                    for (int type = 0; type < jsonArraysList.size(); type++) {
                                        iteratorList(type, jsonArraysList.get(type));
                                        Log.e(TAG, "显示数据");
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, e.getMessage() + "xinxixxxxxx");
                                    e.printStackTrace();
                                }
                            }
                        });


            }
        });


    }


    /*
    * 将jsonarray中的数据存放在Filebox中
    *
    * */
    static public void iteratorList(int type, JSONArray jsonArray) throws JSONException {
        switch (type) {
            case 0:
                for (int count = 0; count < jsonArray.length(); count++) {
                    FileBox.getInstance().addUsbImageItem(JsonUtil.jsonObjectToSenFileInform(jsonArray.getJSONObject(count)));
                }
                break;

            case 1:
                for (int count = 0; count < jsonArray.length(); count++) {
                    FileBox.getInstance().addUsbMusicItem(JsonUtil.jsonObjectToSenFileInform(jsonArray.getJSONObject(count)));
                }
                break;


            case 2:
                for (int count = 0; count < jsonArray.length(); count++) {
                    FileBox.getInstance().addUsbVideoItem(JsonUtil.jsonObjectToSenFileInform(jsonArray.getJSONObject(count)));
                }
                break;

            case 3:
                for (int count = 0; count < jsonArray.length(); count++) {
                    FileBox.getInstance().addUsbDocumentItem(JsonUtil.jsonObjectToSenFileInform(jsonArray.getJSONObject(count)));
                }
                break;
            case 4:
                for (int count = 0; count < jsonArray.length(); count++) {
                    FileBox.getInstance().addUsbOtherItem(JsonUtil.jsonObjectToSenFileInform(jsonArray.getJSONObject(count)));
                }
                break;
        }

    }


    /*
    * 下载usb文件
    *
    *
    * path//服务器路径
    *
    * filename 文件名
    *
    *savepath  文件保存路径
    *
    * */
    static public void downLoadUsbFile(final String path, final String filename, final String savepath, final ProgressBar progressBar, final Handler handler) {
        MainActivity.
                sexecutorService.
                submit(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpUtils
                                .get()
                                .url(path)
                                .build()
                                .execute(new FileCallBack(savepath, filename) {

                                    @Override
                                    public void inProgress(float progress, long total, int id) {
                                        //  super.inProgress(progress, total, id);
                                        progressBar.setProgress((int) (100 * progress));

                                    }

                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        Log.e(TAG, e.getMessage());
                                    }


                                    @Override
                                    public void onResponse(File response, int id) {
                                        handler.sendEmptyMessage(1);               //通知usbfragment将下载文件变为查看文件
                                        Log.e("loginutil", response.getAbsolutePath());


                                    }
                                });


                    }
                });
    }

    /*
    * 退出当前账号
    *
    * */

    static public void exiteAccount() {
        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url(InternetConfig.login_interface)
                        .addParams("act", "quit")
                        .addParams("user_id", MainActivity.user.getUserId())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                config.clearInfor(GetContextUtil.getInstance());
                                Log.e(TAG, "报错");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("is_ok").equals("1")) {

                                        config.clearInfor(GetContextUtil.getInstance());
                                        System.exit(0);

                                    } else {
                                        System.exit(0);
                                        config.clearInfor(GetContextUtil.getInstance());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                            }
                        });
            }
        });

    }

    /*
    * 设置用户的昵称
    * 还有用户的头像
    *
    * */
    static public void setPersonInfor(final String petname, final int portrait) {
        MainActivity.sexecutorService.submit(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        .url(InternetConfig.login_interface)
                        .addParams("act", "update_pic")
                        .addParams("user_id", MainActivity.user.getUserId())
                        .addParams("pic_num", String.valueOf(portrait))
                        .addParams("nick_name", petname)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getString("is_ok").equals("1")) {
                                        MainActivity.user.setPetname(petname);
                                        MainActivity.user.setPortraitId(portrait);
                                        PersonalSettingActivity.handler.sendEmptyMessage(1);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();

                                }

                            }
                        });
            }
        });


    }


}


