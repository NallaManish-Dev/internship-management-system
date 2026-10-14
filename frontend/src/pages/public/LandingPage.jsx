import { ArrowRight, BriefcaseBusiness, Building2, GraduationCap, Search } from 'lucide-react';
import { Link } from 'react-router-dom';
import { Button } from '../../components/common/Button';

export function LandingPage() {
  return <><section className="hero container"><div className="hero-copy"><span className="eyebrow">Internships, made clearer</span><h1>Your career starts with the right internship.</h1><p>Discover opportunities, connect with companies, and take the next step in your career.</p><div className="hero-actions"><Link to="/internships"><Button>Explore internships <ArrowRight size={17} /></Button></Link><Link to="/register/company"><Button variant="secondary">Post an internship</Button></Link></div></div><div className="hero-orb" aria-hidden="true"><BriefcaseBusiness size={78} /></div></section>
  <section className="container how-it-works" aria-labelledby="how-it-works"><div><span className="eyebrow">A focused platform</span><h2 id="how-it-works">Everything you need to move forward</h2></div><div className="feature-grid"><Feature icon={<Search />} title="Discover" text="Find opportunities matched to your ambitions." /><Feature icon={<GraduationCap />} title="Apply with confidence" text="Track every application in one focused workspace." /><Feature icon={<Building2 />} title="Build your team" text="Publish internships and review candidates clearly." /></div></section></>;
}
function Feature({ icon, title, text }) { return <article className="feature-card"><div className="feature-icon">{icon}</div><h3>{title}</h3><p>{text}</p></article>; }
