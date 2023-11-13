import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Button, Col, Nav, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { getUsersAsAdmin } from 'app/modules/administration/user-management/user-management.reducer';

import { faFacebook } from '@fortawesome/free-brands-svg-icons/faFacebook';
import { faInstagram } from '@fortawesome/free-brands-svg-icons/faInstagram';
import { faYoutube } from '@fortawesome/free-brands-svg-icons/faYoutube';
import { faTelegram } from '@fortawesome/free-brands-svg-icons/faTelegram';

export const Footer = () => {
  const getUsersFromProps = () => {};

  const handleSendList = () => {
    getUsersFromProps();
  };

  return (
    <div id="app-footer">
      <section className="footer-contact">
        <Row>
          <Col md="4">
            <h1>Contact Information</h1>
            <div className="info-us">
              <ul>
                <li>
                  <Row>
                    <Col md="1">
                      <a href="https://maps.app.goo.gl/v7sL6xR9uocd21Ja7" target="_blank" title="Location">
                        <FontAwesomeIcon icon="location-dot" className="font-icon" />
                      </a>
                    </Col>
                    <Col md="11">
                      <p>
                        <Translate contentKey="footers.address">Address: 58, Đai Co Viet street, Ha Noi</Translate>
                      </p>
                    </Col>
                  </Row>
                </li>
                <li>
                  <Row>
                    <Col md="1">
                      <a href="https://outlook.office365.com/mail/" target="_blank" title="Email">
                        <FontAwesomeIcon icon="envelope" className="font-icon" />
                      </a>
                    </Col>
                    <Col md="11">
                      <p>
                        <Translate contentKey="footers.mails">Email: nam.dh183804@sis.hust.edu.vn</Translate>
                      </p>
                    </Col>
                  </Row>
                </li>
                <li>
                  <Row>
                    <Col md="1">
                      <a target="_blank" title="Phone">
                        <FontAwesomeIcon icon="phone" className="font-icon" />
                      </a>
                    </Col>
                    <Col md="11">
                      <p>
                        <Translate contentKey="footers.phones">Phone: (+084)328-288-209</Translate>
                      </p>
                    </Col>
                  </Row>
                </li>
              </ul>
            </div>
          </Col>
          <Col md="4">
            <h1>Keep Connected</h1>
            <div className="connect-us">
              <ul>
                <li>
                  <a href="https://www.fb.com/namdh2119" target="_blank" title="Facebook" className="socials-item">
                    <FontAwesomeIcon icon={faFacebook} className="socials-icon facebook" />
                  </a>
                  <p>
                    <Translate contentKey="footers.fl-facebook">Follow me on Facebook</Translate>
                  </p>
                </li>
                <li>
                  <a href="https://www.instagram.com/namdh2119/" target="_blank" title="Instagram" className="socials-item">
                    <FontAwesomeIcon icon={faInstagram} className="socials-icon instagram" />
                  </a>
                  <p>
                    <Translate contentKey="footers.fl-instagram">Follow me on Instagram</Translate>
                  </p>
                </li>
                <li>
                  <a href="https://t.me/namdh183804" target="_blank" title="Telegram" className="socials-item">
                    <FontAwesomeIcon icon={faTelegram} className="socials-icon telegram" />
                  </a>
                  <p>
                    <Translate contentKey="footers.fl-telegram">Message me on Telegram</Translate>
                  </p>
                </li>
                <li>
                  <a
                    href="https://www.youtube.com/channel/UCOZA4d1tRkkEZ3A7D5sFUQA"
                    target="_blank"
                    title="Youtube"
                    className="socials-item"
                  >
                    <FontAwesomeIcon icon={faYoutube} className="socials-icon youtube" />
                  </a>
                  <p>
                    <Translate contentKey="footers.fl-youtube">Subscribe to my channel on Youtube</Translate>
                  </p>
                </li>
              </ul>
            </div>
          </Col>
          <Col md="4">
            <div className="image-us">
              <img src="../../../../content/images/footerDisplay.png" alt="Schedule" />
            </div>
          </Col>
        </Row>
      </section>
    </div>
  );
};

export default Footer;
