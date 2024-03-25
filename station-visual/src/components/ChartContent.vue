<template>
  <div class="content">
    <div>
      <el-table :data="tableData" border style="width: 100%" height="300">
        <el-table-column v-for="(item, index) in tableStruct" :key="index" :prop="item.prop" :label="item.label" :width="item.width" />
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { PropType, computed, defineComponent, onMounted, onUnmounted } from "vue";
export default defineComponent({
  props: {
    stationValue: {
      type: Object as PropType<{
        stationId: string;
        stationName: string;
        type: string;
        data: { stationId: string; time: string; waterLevelValue: number; flowValue: number }[] | { stationId: string; time: string; tideValue: number }[];
      }>,
    },
  },
  setup(props) {


    const tableStruct = computed(() => {
      if (props.stationValue?.type === "flow") {
        return [
          { prop: "time", label: "时间", width: "180" },
          { prop: "waterLevelValue", label: "水位", width: "" },
          { prop: "flowValue", label: "流量", width: "" },
        ];
      } else {
        return [
          { prop: "time", label: "时间", width: "180" },
          { prop: "tideValue", label: "潮位", width: "" },
        ];
      }
    });
    const tableData = computed(() => {
      return props.stationValue?.data;
    });

    return { tableStruct, tableData };
  },
});
</script>

<style scoped lang="scss">
.content {
  background: white;
  height: 300px;
  width: 700px;
}
</style>
