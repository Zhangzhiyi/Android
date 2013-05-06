package com.et.CustomDynamicDataListViewActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;

public class CustomDynamicDataListViewActivity extends Activity {
	
	private ListView mListView;
	private List<String> mContentList;
	public static final int[] ITEM_IN_LINES_ID = {R.id.button1, R.id.button2, R.id.button3, R.id.button4 };
	public static final int ITEM_IN_LINES_LENGTH = ITEM_IN_LINES_ID.length;
	
	public HashMap<Integer, CursorPack> mLineCursorPackMap = new HashMap<Integer, CursorPack>();
	
	public static final int FONT_SIZE = 18;
	
	//平均每个格占得宽度
	public int mItemWidth;
	private Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mListView = (ListView) findViewById(R.id.listView1);
        mContentList = new ArrayList<String>();
        for (int i = 0; i < mStrings.length; i++) {
			mContentList.add(mStrings[i]);
		}
        
        mItemWidth = getScreenWidth(this)/ITEM_IN_LINES_LENGTH;
        
        final DynamicDataAdapter mAdapter = new DynamicDataAdapter(this);
        mListView.setAdapter(mAdapter);
        
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		mContentList.add(null);
        	}
        };
        mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
//				if (firstVisibleItem + visibleItemCount == totalItemCount) {
//					mAdapter.notifyDataSetChanged();
//				}
			}
		});
    }
    
    public int getScreenWidth(Context context){
    	DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
    	return displayMetrics.widthPixels;
    }
    class CursorPack{
    	//一行字符串个数
    	int dataCount;
    	//是否扩展标志
    	boolean[] stretchs;
    	//是否隐藏标志
    	boolean[] collapses;
    	//扩展的个数标志，方便扩展按钮设置多少倍宽度
    	int[] span;
    	public CursorPack() {
			// TODO Auto-generated constructor stub
    		dataCount = 0;
    		stretchs = new boolean[ITEM_IN_LINES_LENGTH];
    		collapses = new boolean[ITEM_IN_LINES_LENGTH];
    		span = new int[ITEM_IN_LINES_LENGTH];
    		Arrays.fill(span, 1);
		}
    	
    }
    public int getDynamicLineCount(){
    	return mContentList.size()/ITEM_IN_LINES_LENGTH + (mContentList.size()/ITEM_IN_LINES_LENGTH != 0 ? 1:0);
    }
    public int oldLineCount = 0;
    public class DynamicDataAdapter extends BaseAdapter{
    	private Context context;
    	private LayoutInflater mInflater;
    	
    	public DynamicDataAdapter(Context context) {
			// TODO Auto-generated constructor stub
    		this.context = context;
    		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			oldLineCount = getDynamicLineCount();
			return oldLineCount;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View viewHolder;
			if (convertView == null) {
				viewHolder = mInflater.inflate(R.layout.tablelayout_item, null);
				
			}else{
				viewHolder = convertView;
			}
			int startNum = 0;
			Set<Integer> keySet = mLineCursorPackMap.keySet();
			Object[] keys =  keySet.toArray();
			Arrays.sort(keys);
			//计算此position之前的字符串个数总数
			for (int i = 0; i < position; i++) {
				if (i > keys.length) {
					break;
				}
				startNum = mLineCursorPackMap.get(keys[i]).dataCount + startNum;
			}
			if (!mLineCursorPackMap.containsKey(position)) {
				CursorPack item = getCursorPack(position, true);
				//可用的列数
				int avaliableColum = ITEM_IN_LINES_LENGTH;
				for (int i = 0; i < ITEM_IN_LINES_LENGTH; i++) {
					
					if (item.collapses[i] == true) {
						item.span[i] = 0;
						continue;
					}
					if (avaliableColum == 0) 
						break;
					int index = startNum + item.dataCount;
					String data = mContentList.get(index);
					if (data == null) {
						item.collapses[i] = true;
						break;
					}
					Button button = (Button) viewHolder.findViewById(ITEM_IN_LINES_ID[i]);
					Paint paint = button.getPaint();
					paint.setTextSize(FONT_SIZE);
					int textWidth = (int) paint.measureText(data);
					int stretchCount = (int) (textWidth/((int)mItemWidth*0.75));
					int differ = avaliableColum - stretchCount;
					
					if (differ == avaliableColum) {
						//不扩展
						item.dataCount ++;
						avaliableColum = avaliableColum - 1;
					}else if(differ > 0){
						//行内扩展
						item.stretchs[i] = true;
						for (int j = i; j < i + stretchCount; j++) {
							item.collapses[j + 1] = true;
							//合并多少个格，就要加上多少空白内容
							//如果在此用add方法会报错，要用handler发送消息到主线程加入空白内容
							handler.sendEmptyMessage(0);
							//如果mContentList是在BaseAdapter里面创建的就不会报错
							//mContentList.add(null);
						}
						item.span[i] = stretchCount + 1;
						item.dataCount ++;
						avaliableColum = avaliableColum - stretchCount -1 ;
					}else if(differ <= 0){
						//全行扩展
						if (i == 0) {
							item.stretchs[i] = true;
							item.span[i] = ITEM_IN_LINES_LENGTH;
							item.dataCount ++ ;
							
							for (int j = 1; j < ITEM_IN_LINES_LENGTH; j++) {
								item.collapses[j] = true;
								handler.sendEmptyMessage(0);
							}
							avaliableColum = 0;
						}else{
							//换行
							for (int j = i; j < ITEM_IN_LINES_LENGTH; j++) {
								item.collapses[j] = true;
								handler.sendEmptyMessage(0);
							}
							//将可用的列数分配给没有隐藏的列
							while(avaliableColum > 0){
								for (int j = 0; j < i; j++) {
									if (!item.collapses[j] && avaliableColum > 0) {
										item.span[j] ++;
										avaliableColum -- ;
									}
								}
							}
						}
					}
				}
				
			}
			//装载数据
			CursorPack item = mLineCursorPackMap.get(position);
			int dataCount = item.dataCount;
			int endNum = startNum + dataCount;
			for (int i = 0; i < ITEM_IN_LINES_LENGTH; i++) {
				TableLayout tableLayout = (TableLayout) viewHolder.findViewById(R.id.tableLayout1);
				Button button = (Button) viewHolder.findViewById(ITEM_IN_LINES_ID[i]);
				
				tableLayout.setColumnStretchable(i, item.stretchs[i]);
				
				tableLayout.setColumnCollapsed(i, item.collapses[i]);
				
				if (item.collapses[i]) {
					continue;
				}
				if (startNum < endNum) {
					
					String content = mContentList.get(startNum);
					startNum ++ ;
					button.setTextSize(TypedValue.COMPLEX_UNIT_PX, FONT_SIZE);
					button.setText(content);
					button.getLayoutParams().width = item.span[i] * mItemWidth;
				}
			}
			if (position == oldLineCount -1) {
				if (oldLineCount != mContentList.size()) {
					notifyDataSetChanged();
				}
			}
			return viewHolder;
		}

    }
    CursorPack getCursorPack( int position, boolean not_contain_new )
	{
		CursorPack cp = null;
		if( !mLineCursorPackMap.containsKey(position) )
		{
			if( not_contain_new)
			{
				cp= new CursorPack();
				mLineCursorPackMap.put(position, cp);
			}
		}
		else
			cp = mLineCursorPackMap.get(position);
		return cp;
	}
    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
            "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert","Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
            "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
            "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
            "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
            "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
            "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
            "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
            "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
            "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
            "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)",
            "Boeren Leidenkaas", "Bonchester", "Bosworth", "Bougon", "Boule Du Roves",
            "Boulette d'Avesnes", "Boursault", "Boursin", "Bouyssou", "Bra", "Braudostur",
            "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
            "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",
            "Brin", "Brin d' Amour", "Brin d'Amour", "Brinza (Burduf Brinza)",
            "Briquette de Brebis", "Briquette du Forez", "Broccio", "Broccio Demi-Affine",
            "Brousse du Rove", "Bruder Basil", "Brusselae Kaas (Fromage de Bruxelles)", "Bryndza",
            "Buchette d'Anjou", "Buffalo", "Burgos", "Butte", "Butterkase", "Button (Innes)",
            "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta",
            "Caerphilly", "Cairnsmore", "Calenzana", "Cambazola", "Camembert de Normandie",
            "Canadian Cheddar", "Canestrato", "Cantal", "Caprice des Dieux", "Capricorn Goat",
            "Capriole Banon", "Carre de l'Est", "Casciotta di Urbino", "Cashel Blue", "Castellano",
            "Castelleno", "Castelmagno", "Castelo Branco", "Castigliano", "Cathelain",
            "Celtic Promise", "Cendre d'Olivet", "Cerney", "Chabichou", "Chabichou du Poitou",
            "Chabis de Gatine", "Chaource", "Charolais", "Chaumes", "Cheddar",
            "Cheddar Clothbound", "Cheshire", "Chevres", "Chevrotin des Aravis", "Chontaleno",
            "Civray", "Coeur de Camembert au Calvados", "Coeur de Chevre", "Colby", "Cold Pack",
            "Comte", "Coolea", "Cooleney", "Coquetdale", "Corleggy", "Cornish Pepper",
            "Cotherstone", "Cotija", "Cottage Cheese", "Cottage Cheese (Australian)",
            "Cougar Gold", "Coulommiers", "Coverdale", "Crayeux de Roncq", "Cream Cheese",
            "Cream Havarti", "Crema Agria", "Crema Mexicana", "Creme Fraiche", "Crescenza",
            "Croghan", "Crottin de Chavignol", "Crottin du Chavignol", "Crowdie", "Crowley",
            "Cuajada", "Curd", "Cure Nantais", "Curworthy", "Cwmtawe Pecorino",
            "Cypress Grove Chevre", "Danablu (Danish Blue)", "Danbo", "Danish Fontina",
            "Daralagjazsky", "Dauphin", "Delice des Fiouves", "Denhany Dorset Drum", "Derby",
            "Dessertnyj Belyj", "Devon Blue", "Devon Garland", "Dolcelatte", "Doolin",
            "Doppelrhamstufel", "Dorset Blue Vinney", "Double Gloucester", "Double Worcester",
            "Dreux a la Feuille", "Dry Jack", "Duddleswell", "Dunbarra", "Dunlop", "Dunsyre Blue",
            "Duroblando", "Durrus", "Dutch Mimolette (Commissiekaas)", "Edam", "Edelpilz",
            "Emental Grand Cru", "Emlett", "Emmental", "Epoisses de Bourgogne", "Esbareich",
            "Esrom", "Etorki", "Evansdale Farmhouse Brie", "Evora De L'Alentejo", "Exmoor Blue",
            "Explorateur", "Feta", "Feta (Australian)", "Figue", "Filetta", "Fin-de-Siecle",
            "Finlandia Swiss", "Finn", "Fiore Sardo", "Fleur du Maquis", "Flor de Guia",
            "Flower Marie", "Folded", "Folded cheese with mint", "Fondant de Brebis",
            "Fontainebleau", "Fontal", "Fontina Val d'Aosta", "Formaggio di capra", "Fougerus",
            "Four Herb Gouda", "Fourme d' Ambert", "Fourme de Haute Loire", "Fourme de Montbrison",
            "Fresh Jack", "Fresh Mozzarella", "Fresh Ricotta", "Fresh Truffles", "Fribourgeois",
            "Friesekaas", "Friesian", "Friesla", "Frinault", "Fromage a Raclette", "Fromage Corse",
            "Fromage de Montagne de Savoie", "Fromage Frais", "Fruit Cream Cheese",
            "Frying Cheese", "Fynbo", "Gabriel", "Galette du Paludier", "Galette Lyonnaise",
            "Galloway Goat's Milk Gems", "Gammelost", "Gaperon a l'Ail", "Garrotxa", "Gastanberra",
            "Geitost", "Gippsland Blue", "Gjetost", "Gloucester", "Golden Cross", "Gorgonzola",
            "Gornyaltajski", "Gospel Green", "Gouda", "Goutu", "Gowrie", "Grabetto", "Graddost",
            "Grafton Village Cheddar", "Grana", "Grana Padano", "Grand Vatel",
            "Grataron d' Areches", "Gratte-Paille", "Graviera", "Greuilh", "Greve",
            "Gris de Lille", "Gruyere", "Gubbeen", "Guerbigny", "Halloumi",
            "Halloumy (Australian)", "Haloumi-Style Cheese", "Harbourne Blue", "Havarti",
            "Heidi Gruyere", "Hereford Hop", "Herrgardsost", "Herriot Farmhouse", "Herve",
            "Hipi Iti", "Hubbardston Blue Cow", "Hushallsost", "Iberico", "Idaho Goatster",
            "Idiazabal", "Il Boschetto al Tartufo", "Ile d'Yeu", "Isle of Mull", "Jarlsberg",
            "Jermi Tortes", "Jibneh Arabieh", "Jindi Brie", "Jubilee Blue", "Juustoleipa",
            "Kadchgall", "Kaseri", "Kashta", "Kefalotyri", "Kenafa", "Kernhem", "Kervella Affine",
            "Kikorangi", "King Island Cape Wickham Brie", "King River Gold", "Klosterkaese",
            "Knockalara", "Kugelkase", "L'Aveyronnais", "L'Ecir de l'Aubrac", "La Taupiniere",
            "La Vache Qui Rit", "Laguiole", "Lairobell", "Lajta", "Lanark Blue", "Lancashire",
            "Langres", "Lappi", "Laruns", "Lavistown", "Le Brin", "Le Fium Orbo", "Le Lacandou",
            "Le Roule", "Leafield", "Lebbene", "Leerdammer", "Leicester", "Leyden", "Limburger",
            "Lincolnshire Poacher", "Lingot Saint Bousquet d'Orb", "Liptauer", "Little Rydings",
            "Livarot", "Llanboidy", "Llanglofan Farmhouse", "Loch Arthur Farmhouse",
            "Loddiswell Avondale", "Longhorn", "Lou Palou", "Lou Pevre", "Lyonnais", "Maasdam",
            "Macconais", "Mahoe Aged Gouda", "Mahon", "Malvern", "Mamirolle", "Manchego",
            "Manouri", "Manur", "Marble Cheddar", "Marbled Cheeses", "Maredsous", "Margotin",
            "Maribo", "Maroilles", "Mascares", "Mascarpone", "Mascarpone (Australian)",
            "Mascarpone Torta", "Matocq", "Maytag Blue", "Meira", "Menallack Farmhouse",
            "Menonita", "Meredith Blue", "Mesost", "Metton (Cancoillotte)", "Meyer Vintage Gouda",
            "Mihalic Peynir", "Milleens", "Mimolette", "Mine-Gabhar", "Mini Baby Bells", "Mixte",
            "Molbo", "Monastery Cheeses", "Mondseer", "Mont D'or Lyonnais", "Montasio",
            "Monterey Jack", "Monterey Jack Dry", "Morbier", "Morbier Cru de Montagne",
            "Mothais a la Feuille", "Mozzarella", "Mozzarella (Australian)",
            "Mozzarella di Bufala", "Mozzarella Fresh, in water", "Mozzarella Rolls", "Munster",
            "Murol", "Mycella", "Myzithra", "Naboulsi", "Nantais", "Neufchatel",
            "Neufchatel (Australian)", "Niolo", "Nokkelost", "Northumberland", "Oaxaca",
            "Olde York", "Olivet au Foin", "Olivet Bleu", "Olivet Cendre",
            "Orkney Extra Mature Cheddar", "Orla", "Oschtjepka", "Ossau Fermier", "Ossau-Iraty",
            "Oszczypek", "Oxford Blue", "P'tit Berrichon", "Palet de Babligny", "Paneer", "Panela",
            "Pannerone", "Pant ys Gawn", "Parmesan (Parmigiano)", "Parmigiano Reggiano",
            "Pas de l'Escalette", "Passendale", "Pasteurized Processed", "Pate de Fromage",
            "Patefine Fort", "Pave d'Affinois", "Pave d'Auge", "Pave de Chirac", "Pave du Berry",
            "Pecorino", "Pecorino in Walnut Leaves", "Pecorino Romano", "Peekskill Pyramid",
            "Pelardon des Cevennes", "Pelardon des Corbieres", "Penamellera", "Penbryn",
            "Pencarreg", "Perail de Brebis", "Petit Morin", "Petit Pardou", "Petit-Suisse",
            "Picodon de Chevre", "Picos de Europa", "Piora", "Pithtviers au Foin",
            "Plateau de Herve", "Plymouth Cheese", "Podhalanski", "Poivre d'Ane", "Polkolbin",
            "Pont l'Eveque", "Port Nicholson", "Port-Salut", "Postel", "Pouligny-Saint-Pierre",
            "Pourly", "Prastost", "Pressato", "Prince-Jean", "Processed Cheddar", "Provolone",
            "Provolone (Australian)", "Pyengana Cheddar", "Pyramide", "Quark",
            "Quark (Australian)", "Quartirolo Lombardo", "Quatre-Vents", "Quercy Petit",
            "Queso Blanco", "Queso Blanco con Frutas --Pina y Mango", "Queso de Murcia",
            "Queso del Montsec", "Queso del Tietar", "Queso Fresco", "Queso Fresco (Adobera)",
            "Queso Iberico", "Queso Jalapeno", "Queso Majorero", "Queso Media Luna",
            "Queso Para Frier", "Queso Quesadilla", "Rabacal", "Raclette", "Ragusano", "Raschera",
            "Reblochon", "Red Leicester", "Regal de la Dombes", "Reggianito", "Remedou",
            "Requeson", "Richelieu", "Ricotta", "Ricotta (Australian)", "Ricotta Salata", "Ridder",
            "Rigotte", "Rocamadour", "Rollot", "Romano", "Romans Part Dieu", "Roncal", "Roquefort",
            "Roule", "Rouleau De Beaulieu", "Royalp Tilsit", "Rubens", "Rustinu", "Saaland Pfarr",
            "Saanenkaese", "Saga", "Sage Derby", "Sainte Maure", "Saint-Marcellin",
            "Saint-Nectaire", "Saint-Paulin", "Salers", "Samso", "San Simon", "Sancerre",
            "Sap Sago", "Sardo", "Sardo Egyptian", "Sbrinz", "Scamorza", "Schabzieger", "Schloss",
            "Selles sur Cher", "Selva", "Serat", "Seriously Strong Cheddar", "Serra da Estrela",
            "Sharpam", "Shelburne Cheddar", "Shropshire Blue", "Siraz", "Sirene", "Smoked Gouda",
            "Somerset Brie", "Sonoma Jack", "Sottocenare al Tartufo", "Soumaintrain",
            "Sourire Lozerien", "Spenwood", "Sraffordshire Organic", "St. Agur Blue Cheese",
            "Stilton", "Stinking Bishop", "String", "Sussex Slipcote", "Sveciaost", "Swaledale",
            "Sweet Style Swiss", "Swiss", "Syrian (Armenian String)", "Tala", "Taleggio", "Tamie",
            "Tasmania Highland Chevre Log", "Taupiniere", "Teifi", "Telemea", "Testouri",
            "Tete de Moine", "Tetilla", "Texas Goat Cheese", "Tibet", "Tillamook Cheddar",
            "Tilsit", "Timboon Brie", "Toma", "Tomme Brulee", "Tomme d'Abondance",
            "Tomme de Chevre", "Tomme de Romans", "Tomme de Savoie", "Tomme des Chouans", "Tommes",
            "Torta del Casar", "Toscanello", "Touree de L'Aubier", "Tourmalet",
            "Trappe (Veritable)", "Trois Cornes De Vendee", "Tronchon", "Trou du Cru", "Truffe",
            "Tupi", "Turunmaa", "Tymsboro", "Tyn Grug", "Tyning", "Ubriaco", "Ulloa",
            "Vacherin-Fribourgeois", "Valencay", "Vasterbottenost", "Venaco", "Vendomois",
            "Vieux Corse", "Vignotte", "Vulscombe", "Waimata Farmhouse Blue",
            "Washed Rind Cheese (Australian)", "Waterloo", "Weichkaese", "Wellington",
            "Wensleydale", "White Stilton", "Whitestone Farmhouse", "Wigmore", "Woodside Cabecou",
            "Xanadu", "Xynotyro", "Yarg Cornish", "Yarra Valley Pyramid", "Yorkshire Blue",
            "Zamorano", "Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"};
}