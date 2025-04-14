package xyz.sadiulhakim.transaction;

public enum Currency {
	US_DOLLAR(1, "$", "USD"),
	EURO(2, "€", "EUR"),
	BRITISH_POUND(3, "£", "GBP"),
	JAPANESE_YEN(4, "¥", "JPY"),
	CHINESE_YUAN(5, "¥", "CNY"),
	INDIAN_RUPEE(6, "₹", "INR"),
	BANGLADESHI_TAKA(7, "৳", "BDT"),
	CANADIAN_DOLLAR(8, "$", "CAD"),
	AUSTRALIAN_DOLLAR(9, "$", "AUD"),
	SWISS_FRANC(10, "CHF", "CHF"),
	RUSSIAN_RUBLE(11, "₽", "RUB"),
	SOUTH_KOREAN_WON(12, "₩", "KRW"),
	SINGAPORE_DOLLAR(13, "$", "SGD"),
	MALAYSIAN_RINGGIT(14, "RM", "MYR"),
	SAUDI_RIYAL(15, "﷼", "SAR"),
	UAE_DIRHAM(16, "د.إ", "AED"),
	SOUTH_AFRICAN_RAND(17, "R", "ZAR"),
	TURKISH_LIRA(18, "₺", "TRY"),
	BRAZILIAN_REAL(19, "R$", "BRL"),
	MEXICAN_PESO(20, "$", "MXN");

	private final int id;
	private final String symbol;
	private final String isoCode;

	Currency(int id, String symbol, String isoCode) {
		this.id = id;
		this.symbol = symbol;
		this.isoCode = isoCode;
	}

	public int getId() {
		return id;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getIsoCode() {
		return isoCode;
	}
	
	public static Currency getById(int id) {
		for (var type : values()) {
			if (type.getId() == id)
				return type;
		}
		return null;
	}
	
	public static Currency getByCode(String code) {
		for (var type : values()) {
			if (type.getIsoCode().equals(code))
				return type;
		}
		return null;
	}

	public static Currency get(String text) {

		try {
			int id = Integer.parseInt(text);
			return getById(id);
		} catch (NumberFormatException e) {
			return getByCode(text);
		}
	}
}
