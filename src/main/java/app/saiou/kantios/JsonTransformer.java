package app.saiou.kantios;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private Gson render = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss.SSS").create();
    private Gson gson = new Gson();

    @Override
    public String render(Object model) throws Exception {
        return render.toJson(model);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
