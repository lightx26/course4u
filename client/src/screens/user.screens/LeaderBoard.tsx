
import React, { useState } from "react";
import logo from "../../assets/images/logo.jpg";
import Card_LeaderBoard from "../../components/user.components/Card_LeaderBoard";
import { Table,Select } from "antd";
import type { TableProps } from "antd";
import "../../assets/css/Leader_board.css";
const LeaderBoard: React.FC = () => {
  const [valueYear, setValueYear] = useState<string>("2024");
  interface DataType {
    key: string;
    rank: number;
    username: object;
    time: number;
    score: number;
  }

  const columns: TableProps<DataType>["columns"] = [
    {
      title: "Rank",
      dataIndex: "rank",
      key: "rank",
      width: 100,
      render: (text) => <p style={{ fontWeight: "500" }}>{text}</p>,
    },
    {
      title: "Username",
      dataIndex: "username",
      key: "username",
      render: (data: { avatarUrl: string; username: string }) => {
        return (
          <>
            <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
              <div
                style={{
                  position: "relative",
                  width: "30px",
                  height: "30px",
                  borderRadius: "50%",
                }}
              >
                <img
                  src={data.avatarUrl}
                  alt=""
                  style={{
                    position: "absolute",
                    width: "100%",
                    height: "100%",
                    left: 0,
                    right: 0,
                    objectFit: "cover",
                    objectPosition: "center",
                    borderRadius: "50%",
                  }}
                />
              </div>
              <div style={{ fontWeight: 400 }}>{data.username}</div>
            </div>
          </>
        );
      },
    },
    {
      title: "Time",
      dataIndex: "time",
      key: "time",
      render: (text) => <p style={{ fontSize: "1rem" }}>{text}</p>,
    },
    {
      title: "Score",
      dataIndex: "score",
      key: "score",
      render: (text) => <p style={{ fontSize: "1rem" }}>{text}</p>,
    },
  ];
  const data: DataType[] = [
    {
      key: "1",
      rank: 4,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "2",
      rank: 5,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "3",
      rank: 6,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "4",
      rank: 7,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "5",
      rank: 8,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "6",
      rank: 9,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
    {
      key: "7",
      rank: 10,
      username: {
        avatarUrl: logo,
        username: "NguyenThucHoang",
      },
      time: 44,
      score: 60,
    },
  ];


  const handleChange = (value: string) => {
    setValueYear(value);
  };
  return (
    <>
      <div style={{ backgroundColor: "#f5f5f5", paddingBottom: "50px" }}>
        <div
          style={{
            maxWidth: "1536px",
            margin: "0 auto",
            padding: "25px 20px 0px 20px",
          }}
        >
          <div
            style={{
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "0px 10px",
            }}
          >
            <div>
              <span style={{ fontSize: "2rem", fontWeight: 600 }}>
                Ranking of
              </span>
              <span
                style={{ fontSize: "2rem", fontWeight: 600, color: "#861fa2" }}
              >
                {" "}
                Year 2024
              </span>
            </div>
            <div
              style={{
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                gap: "25px",
              }}
            >
              <div style={{ color: "#666666", fontSize: "1.1rem" }}>Year</div>
              <div>
              <Select
      defaultValue={valueYear}
      style={{ width: 120 }}
      onChange={handleChange}
      options={[
        { value: '2024', label: '2024' },
        { value: '2023', label: '2023' },
        { value: '2022', label: '2022' },
      ]}
    />
              </div>
            </div>
          </div>
          <div
            style={{
              display: "flex",
              justifyContent: "center",
              gap: "30px",
              marginTop: "40px",
              alignItems: "flex-end",
            }}
          >
            <Card_LeaderBoard
              username="NguyenThucHoang"
              email="hoang.thuc.nguyen@mgm-tp.com"
              ranking={2}
              avatarUrl={logo}
              score={8888}
              learningTime={160}
            />
            <Card_LeaderBoard
              username="NguyenThucHoang"
              email="hoang.thuc.nguyen@mgm-tp.com"
              ranking={1}
              avatarUrl={logo}
              score={8888}
              learningTime={160}
            />
            <Card_LeaderBoard
              username="NguyenThucHoang"
              email="hoang.thuc.nguyen@mgm-tp.com"
              ranking={3}
              avatarUrl={logo}
              score={8888}
              learningTime={160}
            />
          </div>
          <div style={{ marginTop: "50px" }}>
            <Table columns={columns} dataSource={data} pagination={false} />
          </div>
        </div>
      </div>
    </>
  );
};
export default LeaderBoard;
