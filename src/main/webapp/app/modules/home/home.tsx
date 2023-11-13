import './home.scss';

import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const [currentTime, setCurrentTime] = useState(new Date());

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000); // Cập nhật thời gian mỗi giây

    return () => {
      clearInterval(intervalId); // Dừng interval khi component unmounts
    };
  }, []);

  const week = ['SUN', 'MON', 'TUE', 'WED', 'THU', 'FRI', 'SAT'];

  const time =
    currentTime.getHours().toString().padStart(2, '0') +
    ':' +
    currentTime.getMinutes().toString().padStart(2, '0') +
    ':' +
    currentTime.getSeconds().toString().padStart(2, '0');
  const date =
    currentTime.getFullYear().toString().padStart(4, '0') +
    '-' +
    (currentTime.getMonth() + 1).toString().padStart(2, '0') +
    '-' +
    currentTime.getDate().toString().padStart(2, '0') +
    ' ' +
    week[currentTime.getDay()];

  return (
    <div id="app-home" className="home-class">
      <section className="welcome">
        <Row>
          <Col md="8">
            <div className="heading-main-box">
              <h2>
                <Translate contentKey="home.title">Welcome to the Class Schedule Website!</Translate>
              </h2>
              <div className="home-text">
                <Translate contentKey="home.subtitle">
                  We are extremely excited for you to come and use our services. This website is designed to help you manage and organize
                  your study time easily and effectively. Regardless of whether you are a student, teacher, or classroom manager, we hope
                  that this website will meet and serve your needs.
                </Translate>
              </div>
            </div>
          </Col>
          <Col md="4">
            <div className="clock">
              <div className="date">{date}</div>
              <div className="time">{time}</div>
            </div>
          </Col>
        </Row>
      </section>
      <section className="about-service">
        <h1>About Service</h1>
        <div className="introduce">
          <Translate contentKey="home.about-services">This is about our service</Translate>
        </div>
        <Row className="service-row">
          <Col md="4">
            <div className="service-image-ct">
              <div className="service-image">
                <img src="../../../content/images/aboutServiceSchedule.png" alt="Classes schedule" />
              </div>
              <div className="service-title">
                <div className="title-name">
                  <Translate contentKey="home.service-title.name-1">Schedule</Translate>
                </div>
                <div className="title-detail">
                  <Translate contentKey="home.service-title.detail-1">
                    Schedule detail Schedule detail Schedule detail Schedule detail
                  </Translate>
                </div>
              </div>
            </div>
          </Col>
          <Col md="4">
            <div className="service-image-ct">
              <div className="service-image">
                <img src="../../../content/images/aboutServiceImExport.jpg" alt="Classes schedule" />
              </div>
              <div className="service-title">
                <div className="title-name">
                  <Translate contentKey="home.service-title.name-3">Import & Export Data</Translate>
                </div>
                <div className="title-detail">
                  <Translate contentKey="home.service-title.detail-3">
                    Schedule detail Schedule detail Schedule detail Schedule detail
                  </Translate>
                </div>
              </div>
            </div>
          </Col>
          <Col md="4">
            <div className="service-image-ct">
              <div className="service-image">
                <img src="../../../content/images/aboutServiceCRUD.jpeg" alt="Classes schedule" />
              </div>
              <div className="service-title">
                <div className="title-name">
                  <Translate contentKey="home.service-title.name-2">CRUD</Translate>
                </div>
                <div className="title-detail">
                  <Translate contentKey="home.service-title.detail-2">
                    Schedule detail Schedule detail Schedule detail Schedule detail
                  </Translate>
                </div>
              </div>
            </div>
          </Col>
        </Row>
      </section>
      <section className="user-manual">
        <h1>User manual</h1>
        <Row className="user-manual-row">
          <Col md="6">
            <ul>
              <li>
                <h4>
                  <Translate contentKey="home.user-manual.step-1">Step 1:</Translate>
                </h4>
                <div className="step-title">
                  <Translate contentKey="home.user-manual.step-title-1">Register & Sign In</Translate>
                </div>
                <div className="step-detail">
                  <Translate contentKey="home.user-manual.step-detail-1">
                    Register a new account, wait for administrator approval. Sign in to the system with an approved account.
                  </Translate>
                </div>
              </li>
              <li>
                <h4>
                  <Translate contentKey="home.user-manual.step-2">Step 2:</Translate>
                </h4>
                <div className="step-title">
                  <Translate contentKey="home.user-manual.step-title-2">Register & Sign In</Translate>
                </div>
                <div className="step-detail">
                  <Translate contentKey="home.user-manual.step-detail-2">
                    Register a new account, wait for administrator approval. Sign in to the system with an approved account.
                  </Translate>
                </div>
              </li>
              <li>
                <h4>
                  <Translate contentKey="home.user-manual.step-3">Step 3:</Translate>
                </h4>
                <div className="step-title">
                  <Translate contentKey="home.user-manual.step-title-3">Register & Sign In</Translate>
                </div>
                <div className="step-detail">
                  <Translate contentKey="home.user-manual.step-detail-3">
                    Register a new account, wait for administrator approval. Sign in to the system with an approved account.
                  </Translate>
                </div>
              </li>
            </ul>
          </Col>
          <Col md="3">
            <div className="image1">
              <img src="../../../content/images/technologyHand.jpg" alt="activities4" />
            </div>
          </Col>
          <Col md="3">
            <div className="image2">
              <img src="../../../content/images/shakeHand.jpg" alt="activities4" />
            </div>
            <div className="image3">
              <img src="../../../content/images/solutionHand.jpg" alt="activities4" />
            </div>
          </Col>
        </Row>
      </section>
    </div>
  );
};

export default Home;
