export interface ResponseType {
  code: number;
  msg: string;
  data: any;
}

export interface StationInfo {
  tide: { stationName: string; stationId: string; lon: number; lat: number }[];
  flow: { stationName: string; stationId: string; lon: number; lat: number }[];
}
