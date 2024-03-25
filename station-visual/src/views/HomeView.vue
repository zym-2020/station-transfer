<template>
  <div class="map" ref="container"></div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, createApp, reactive } from "vue";
import ElementPlus from "element-plus";
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import mapBoxGl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import axios from "axios";
import { StationInfo } from "@/type";
import { getTideData, getFlowData } from "@/api/request";
import ChartContent from "@/components/ChartContent.vue";
export default defineComponent({
  components: { ChartContent },
  setup() {
    const container = ref<HTMLDivElement>();
    let map: mapBoxGl.Map;
    let popupDiv: HTMLDivElement | null = null;

    const stationValue = reactive<{
      stationId: string;
      stationName: string;
      type: string;
      data: { stationId: string; time: string; waterLevelValue: number; flowValue: number }[] | { stationId: string; time: string; tideValue: number }[];
    }>({
      stationId: "",
      stationName: "",
      type: "",
      data: [],
    });

    const generateFeatures = (stationInfo: StationInfo) => {
      const features: any[] = [];
      stationInfo.tide.forEach((tide) => {
        features.push({
          type: "Feature",
          geometry: {
            type: "Point",
            coordinates: [tide.lon, tide.lat],
          },
          properties: {
            stationName: tide.stationName,
            stationId: tide.stationId,
            type: "tide",
          },
        });
      });
      stationInfo.flow.forEach((flow) => {
        features.push({
          type: "Feature",
          geometry: {
            type: "Point",
            coordinates: [flow.lon, flow.lat],
          },
          properties: {
            stationName: flow.stationName,
            stationId: flow.stationId,
            type: "flow",
          },
        });
      });
      return features;
    };

    const createPopup = () => {
      const app = createApp(ChartContent, { stationValue: stationValue }).use(ElementPlus, {
        locale: zhCn,
      });
      const parent = document.createElement("div");
      app.mount(parent);
      popupDiv = parent;
    };

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value!,
        style: "mapbox://styles/johnnyt/clblx2t3v000a14proaq4e9qv",
        center: [121.024075, 31.765318],
        zoom: 8.8,
        antialias: true,
        accessToken: "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg",
      });
      map.on("load", () => {
        map.loadImage("/station.png", async (err, image) => {
          if (err) throw err;
          map.addImage("station", image!);
          const stationInfo: StationInfo = await axios.get("/station.json").then((res) => res.data);

          const features = generateFeatures(stationInfo);
          map.addSource("points", {
            type: "geojson",
            data: {
              type: "FeatureCollection",
              features: features,
            },
          });
          map.addLayer({
            id: "station",
            type: "symbol",
            source: "points",
            layout: {
              "icon-size": 0.2,
              "icon-image": "station",
              "text-field": ["get", "stationName"],
              "text-font": ["Open Sans Semibold", "Arial Unicode MS Bold"],
              "text-offset": [0, 1.25],
              "text-anchor": "top",
              "icon-allow-overlap": true,
              "icon-ignore-placement": true,
            },
            paint: {
              "text-color": "white",
            },
          });
          map.on("mouseenter", "station", () => {
            map.getCanvas().style.cursor = "pointer";
          });
          map.on("mouseleave", "station", () => {
            map.getCanvas().style.cursor = "";
          });
          map.on("click", "station", async (e) => {
            const coordinates = (e.features![0].geometry as any).coordinates.slice();
            const stationName = e.features![0].properties!.stationName;
            const stationId = e.features![0].properties!.stationId;
            const type = e.features![0].properties!.type;
            const endTime = new Date().getTime();
            const startTime = endTime - 24 * 60 * 60 * 1000;
            const data = await getTideData(stationId, startTime.toString(), endTime.toString());
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
              coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }
            stationValue.stationId = stationId;
            stationValue.stationName = stationName;
            stationValue.type = type;
            stationValue.data = data?.data;
            if (!popupDiv) {
              createPopup();
            }
            new mapBoxGl.Popup().setMaxWidth("800px").setLngLat(coordinates).setDOMContent(popupDiv!).addTo(map);
          });
        });
      });
    };
    onMounted(initMap);
    return { container };
  },
});
</script>

<style lang="scss" scoped>
.map {
  height: 100%;
}
.hover-cursor {
  cursor: pointer;
}
</style>
