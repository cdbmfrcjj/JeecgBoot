<script lang="ts" setup name="OpenReport">
// 外部打开积木报表并与系统使用统一身份认证
// http://localhost:9020/openReport/953070792952979456?description=%E9%A9%AC%E6%A1%A5%E6%B2%B3%E6%9E%97%E5%9C%BA
import { ref } from 'vue';
import { getToken } from '/@/utils/auth';
import { useRoute } from 'vue-router';
const route = useRoute(); // 解构路由对象
const reportId = route.params.reportId;
const token = getToken();
const queryParams = route.query;
const urlParams = Object.keys(queryParams)
  .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(queryParams[key])}`)
  .join('&');
let url = `${window._CONFIG['domianURL']}/jmreport/view/${reportId}?token=${token}&${urlParams}`;
window.location.href = url;
</script>
