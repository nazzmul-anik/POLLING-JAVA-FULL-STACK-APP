import axiosInstance from "../../environment/AxiosInstance";

export const postPoll = async (pollDTO) => {
  const response = await axiosInstance.post("/api/user/poll", pollDTO);
  return response;
};

export const getAllPolls = async () => {
  const response = await axiosInstance.get("/api/user/polls");
  return response;
};

export const getMyPolls = async () => {
  const response = await axiosInstance.get("/api/user/my-polls");
  return response;
};

export const deletePollById = async (id) => {
  const response = await axiosInstance.delete(`/api/user/poll/${id}`);
  return response;
};
