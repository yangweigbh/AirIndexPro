package com.yangwei.airindexpro.datasource;

public class CityAirQualityIndex {

	int aqi;
	String area;
	int pm2_5;
	int pm2_5_24h;
	String position_name;
	String primary_pollutant;
	String quality;
	String station_code;
	String time_point;

	public CityAirQualityIndex(Builder builder) {
		super();
		this.aqi = builder.aqi;
		this.area = builder.area;
		this.pm2_5 = builder.pm2_5;
		this.pm2_5_24h = builder.pm2_5_24h;
		this.position_name = builder.position_name;
		this.primary_pollutant = builder.primary_pollutant;
		this.quality = builder.quality;
		this.station_code = builder.station_code;
		this.time_point = builder.time_point;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
	}

	public int getPm2_5() {
		return pm2_5;
	}

	public void setPm2_5(int pm2_5) {
		this.pm2_5 = pm2_5;
	}

	public int getPm2_5_24h() {
		return pm2_5_24h;
	}

	public void setPm2_5_24h(int pm2_5_24h) {
		this.pm2_5_24h = pm2_5_24h;
	}

	public String getPosition_name() {
		return position_name;
	}

	public void setPosition_name(String position_name) {
		this.position_name = position_name;
	}

	public String getPrimary_pollutant() {
		return primary_pollutant;
	}

	public void setPrimary_pollutant(String primary_pollutant) {
		this.primary_pollutant = primary_pollutant;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getStation_code() {
		return station_code;
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}

	public String getTime_point() {
		return time_point;
	}

	public void setTime_point(String time_point) {
		this.time_point = time_point;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[ aqi: " + aqi).append(", pm2_5: " + aqi)
				.append(", pm2_5: " + aqi).append(", pm2_5_24h: " + pm2_5_24h)
				.append(", position_name: " + position_name).append(", primary_pollutant: " + primary_pollutant)
				.append(", quality: " + quality).append(", station_code: " + station_code)
				.append(", time_point: " + time_point + "]");
		return stringBuilder.toString();
	}

	public static class Builder {
		int aqi;
		String area;
		int pm2_5;
		int pm2_5_24h;
		String position_name;
		String primary_pollutant;
		String quality;
		String station_code;
		String time_point;

		public Builder() {
			super();
		}

		public Builder aqi(int aqi) {
			this.aqi = aqi;
			return this;
		}

		public Builder area(String area) {
			this.area = area;
			return this;
		}

		public Builder pm2_5(int pm2_5) {
			this.pm2_5 = pm2_5;
			return this;
		}

		public Builder pm2_5_24h(int pm2_5_24h) {
			this.pm2_5_24h = pm2_5_24h;
			return this;
		}

		public Builder position_name(String position_name) {
			this.position_name = position_name;
			return this;
		}

		public Builder primary_pollutant(String primary_pollutant) {
			this.primary_pollutant = primary_pollutant;
			return this;
		}

		public Builder quality(String quality) {
			this.quality = quality;
			return this;
		}

		public Builder station_code(String station_code) {
			this.station_code = station_code;
			return this;
		}

		public Builder time_point(String time_point) {
			this.time_point = time_point;
			return this;
		}

		public CityAirQualityIndex build() {
			return new CityAirQualityIndex(this);
		}
	}
}
