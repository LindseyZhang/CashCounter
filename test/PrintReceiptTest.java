package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import logic.GoodInfo;
import logic.GoodSellInfo;
import logic.PrintReceipt;
import logic.PrintReceiptSellByCategory;
import logic.PromotionType;

public class PrintReceiptTest {
	
//	PrintReceipt test_item;
	PrintReceiptSellByCategory test_item;
	
	@Before
	public void Init(){
		
		String json ="[{ barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 3.00 }," +
				"{ barcode: 'ITEM000001', name: '雪碧', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 2.50 }] ";
		String promotion_json = "{ type: 'SELL_BY_CATEGORY_BY_TEN', barcodes: [ 'ITEM000000', 'ITEM000001' ] }";
		test_item = new PrintReceiptSellByCategory(json, promotion_json, 0.95);
				
	}

	@Test
	public void LoadGoodInfoTest() {
		String json ="[{ barcode: 'ITEM000000', name: '可口可乐', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 3.00 }," +
				"{ barcode: 'ITEM000001', name: '雪碧', unit: '瓶', category: '食品', subCategory: '碳酸饮料', price: 2.50 }] ";
		test_item.LoadGoodInfo(json);
		Map<String, GoodInfo> good_infos = test_item.getGood_infos_map();
				
		String barcode;
		GoodInfo good_info;
		for (Map.Entry<String, GoodInfo> entry : good_infos.entrySet()) {
			barcode = entry.getKey();
			good_info = entry.getValue();
			System.out.println("the map key is : " + barcode
					+ " || the value is : " + good_info.toString());
		} 
	}
	
	@Test
	public void ParseInputToJsonTest(){
		List<String> input = new ArrayList<String>();
		input.add("ITEM000001");
		input.add("ITEM000001-2");
		String result =test_item.ParseInputToJson(input);
		System.out.println(result);
	}
	
	@Test
	public void CalcGoodPriceTest(){	
		// test 1: num < 10 
		GoodInfo good1 = new GoodInfo("ITEM000000","可口可乐","瓶","食品","碳酸饮料",3.00);	
		GoodSellInfo good_selled = new GoodSellInfo(good1,5);
		test_item.CalcGoodPrice(good_selled);
		System.out.println(good_selled.toString());
		
		//test 2 : num > 10
		GoodInfo good2 = new GoodInfo("ITEM000000","可口可乐","瓶","食品","碳酸饮料",3.00);	
		GoodSellInfo good_selled2 = new GoodSellInfo(good2,15);
		test_item.CalcGoodPrice(good_selled2);
		System.out.println(good_selled2.toString());
	}
	
	@Test
	public void ParseJsonToReceiptTest(){		
		List<String> input = new ArrayList<String>();
		input.add("ITEM000001");
		input.add("ITEM000002-2");
		input.add("ITEM000002-12");
		String json_str =test_item.ParseInputToJson(input);
		
		String receipt = test_item.ParseJsonToReceipt(json_str);
		System.out.println(receipt);

	}
	
	@Test
	public void PrintReceiptSellByCategoryTest(){	
		System.out.println("test 1: 有符合“按类批发价出售”优惠条件的商品时");
		List<String> input = new ArrayList<String>();
		input.add("ITEM000000");
		input.add("ITEM000001-12");
		String json_str =test_item.ParseInputToJson(input);
		String receipt = test_item.ParseJsonToReceipt(json_str);
		System.out.println(receipt);
		
		System.out.println("\n\ntest 2: 无符合“按类批发价出售”优惠条件的商品时");
		List<String> input2 = new ArrayList<String>();
		input2.add("ITEM000000");
		input2.add("ITEM000001-7");
		String json_str2 =test_item.ParseInputToJson(input2);
		String receipt2 = test_item.ParseJsonToReceipt(json_str2);
		System.out.println(receipt2);
		
	}
	
}
