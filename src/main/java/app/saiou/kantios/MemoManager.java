package app.saiou.kantios;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.saiou.kantios.auth.Authenticator.UserInfo;
import app.saiou.kantios.entity.TbMemo;
import app.saiou.kantios.model.Memo;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class MemoManager {

	private static Logger _logger = LoggerFactory.getLogger(MemoManager.class);

	public static void define() {

		JsonTransformer jsonTransformer = new JsonTransformer();

		BeanFactory factory = BeanFactory.of("app/saiou/kantios/beanDef.xml");
		Memo memoModel = factory.getBean("memoModel");
		
		get("/memo", (req, res) -> {
			UserInfo userInfo = Authentication.getUserInfo(req);
			String userName = userInfo.getUserName();

			Map<String, Object> model = new HashMap<>();
			model.put("userName", userName);

			return new ModelAndView(model, "memo");
		}, new ThymeleafTemplateEngine(Main.getTemplateResolver()));
		
		path("/api", () -> {
			path("/memo", () -> {
				
				// 全件取得
				get("/list", (req, res) -> {
					return memoModel.list(Authentication.getUserInfo(req));
				}, jsonTransformer);
				
				// １件検索
				get("/:memoId", (req, res) -> {
					String memoId = req.params("memoId");
					return memoModel.find(memoId, Authentication.getUserInfo(req));
				}, jsonTransformer);
				
				// 追加
				post("", (req, res) -> {
					String postData = req.body();
					TbMemo memo = jsonTransformer.fromJson(postData, TbMemo.class);
					TbMemo createdMemo = memoModel.create(memo, Authentication.getUserInfo(req));
					return createdMemo;
				}, jsonTransformer);
				
				// 更新
				put("/:memoId", (req, res) -> {
					String memoId = req.params("memoId");
					String putData = req.body();
					TbMemo memo = jsonTransformer.fromJson(putData, TbMemo.class);
					memo.setMemoId(memoId);
					UserInfo userInfo = Authentication.getUserInfo(req);
					TbMemo updatedMemo = memoModel.update(memo, userInfo);
					return updatedMemo;
				}, jsonTransformer);
				
				// 削除
				delete("/:memoId", (req, res) -> {
					String memoId = req.params("memoId");
					UserInfo userInfo = Authentication.getUserInfo(req);
					memoModel.delete(memoId, userInfo);
					
					Map<String, String> map = new HashMap<>();
					map.put("result", "success!");
					
					return map;
				}, jsonTransformer);
			});
		});
	}

}
