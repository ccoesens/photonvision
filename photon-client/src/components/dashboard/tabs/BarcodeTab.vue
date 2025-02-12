<script setup lang="ts">
import { useCameraSettingsStore } from "@/stores/settings/CameraSettingsStore";
import { type BarcodePipelineSettings, type ObjectDetectionPipelineSettings, PipelineType, BarcodeType } from "@/types/PipelineTypes";
import { computed, getCurrentInstance } from "vue";
import { useStateStore } from "@/stores/StateStore";

// TODO fix pipeline typing in order to fix this, the store settings call should be able to infer that only valid pipeline type settings are exposed based on pre-checks for the entire config section
// Defer reference to store access method
const currentPipelineSettings = computed<BarcodePipelineSettings>(
  () => useCameraSettingsStore().currentPipelineSettings as BarcodePipelineSettings
);

const interactiveCols = computed(() =>
  (getCurrentInstance()?.proxy.$vuetify.breakpoint.mdAndDown || false) &&
  (!useStateStore().sidebarFolded || useCameraSettingsStore().isDriverMode)
    ? 9
    : 8
);

</script>

<template>
  <div v-if="currentPipelineSettings.pipelineType === PipelineType.Barcode">
    <pv-select
      v-model="currentPipelineSettings.barcodeType"
      label="Target family"
      :items="Object.values(BarcodeType).filter((type) => typeof type === 'string')"
      :select-cols="interactiveCols"
      @input="(value) => useCameraSettingsStore().changeCurrentPipelineSetting({ barcodeType: value }, false)"
    />
  </div>
</template>
