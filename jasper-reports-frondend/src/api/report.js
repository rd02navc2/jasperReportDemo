import axios from "axios";

export const downloadReport = (format) => {
  return axios.get(`/api/reports/items/${format}`, {
    responseType: "blob",
  });
};
