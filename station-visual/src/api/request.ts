import { get, post, del, patch } from "./axios-config";

export const getFlowData = async (stationId: string, startTime: string, endTime: string) => {
  return await get(`/flow/getData/${stationId}/${startTime}/${endTime}`, true);
};

export const getTideData = async (stationId: string, startTime: string, endTime: string) => {
  return await get(`/tide/getData/${stationId}/${startTime}/${endTime}`, true);
};

export const flowPageQuery = async (stationId: string, page: number, size: number) => {
  return await get(`/flow/pageQuery/${stationId}/${page}/${size}`, true);
};

export const tidePageQuery = async (stationId: string, page: number, size: number) => {
  return await get(`/tide/pageQuery/${stationId}/${page}/${size}`, true);
};
